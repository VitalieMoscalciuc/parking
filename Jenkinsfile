pipeline {
  agent {
    kubernetes {
      yaml '''
        apiVersion: v1
        kind: Pod
        spec:
          containers:
          - name: docker
            image: docker:24.0-dind
            securityContext:
              privileged: true
          - name: custom-ubuntu
            image: nexus.tool.mddinternship.com/custom-ubuntu
            tty: true
            securityContext:
              privileged: true
        '''
    }
  }

  tools {
    maven 'maven-3.9'
  }

  environment {
    PL_PGSQL_URL=credentials('PL_PGSQL_URL')
    PL_PGSQL_DATABASE=credentials('PL_PGSQL_DATABASE')
    PL_PGSQL_USERNAME=credentials('PL_PGSQL_USERNAME')
    PL_PGSQL_PASSWORD=credentials('PL_PGSQL_PASSWORD')
    SONAR_PARKINGLOT_TOKEN=credentials('sonarqube-parkinglot-token')
    WEBHOOK=credentials('PL_WEBHOOK')
  }

  options {
    office365ConnectorWebhooks([[
      name: "Office 365",
      notifyBackToNormal: true, 
      notifyFailure: true,
      notifyRepeatedFailure: true,
      notifySuccess: true,
      notifyAborted: true
      ]])
  }

  stages {
    stage('Set variables') {
      when {
        expression {
          BRANCH_NAME == 'dev' || BRANCH_NAME == 'main'
        }
      }
      steps {
        script {
          sh '''
          sed -i "s#^spring.datasource.url=.*#spring.datasource.url=${PL_PGSQL_URL}/${PL_PGSQL_DATABASE}#" src/main/resources/application.properties
          sed -i "s#^spring.datasource.username=.*#spring.datasource.username=${PL_PGSQL_USERNAME}#" src/main/resources/application.properties
          sed -i "s#^spring.datasource.password=.*#spring.datasource.password=${PL_PGSQL_PASSWORD}#" src/main/resources/application.properties
          awk -F"[<>]" '/<version>[0-9]+\\.[0-9]+\\.[0-9]+-SNAPSHOT<\\/version>/{gsub("-SNAPSHOT", "", $3); print $3; exit}' pom.xml > ./temp.txt
          '''
          env.VERSION = readFile('./temp.txt').trim()
        }
      }
    }
    stage('SonarQube analysis'){
      when {
        expression {
          BRANCH_NAME == 'dev' || BRANCH_NAME == 'main'
        }
      }
      steps {
        container('custom-ubuntu') {
          script {
            withSonarQubeEnv(credentialsId: 'sonarqube-parkinglot-token', installationName: 'SonarQube') {
              sh 'mvn clean install sonar:sonar'
            }
          }
        }
      }
    }
    stage('Quality Gate'){
      when {
        expression {
          BRANCH_NAME == 'dev' || BRANCH_NAME == 'main'
        }
      }
      steps {
        container('custom-ubuntu') {
          script {
            withSonarQubeEnv(credentialsId: 'sonarqube-parkinglot-token', installationName: 'SonarQube') {
              sh 'apt-get update && apt-get install -y jq'
              def qualityGateStatus = sh(
                script: '''
                curl -s -u "${SONAR_PARKINGLOT_TOKEN}:" "https://sonarqube.tool.mddinternship.com/api/qualitygates/project_status?projectKey=com.endava:parking-lot" | jq -r '.projectStatus.status'
                ''',
                returnStdout: true
              ).trim()
              echo "Quality Gate Status: ${qualityGateStatus}"
              if (qualityGateStatus != 'OK') {
                error "Pipeline aborted due to quality gate failure: ${qualityGateStatus}"
              }
            }
          }
        }
      }
    }
    stage('Build the application and Docker images') {
      when {
        expression {
          BRANCH_NAME == 'dev'
        }
      }
      steps {
        echo 'building...'
        container('docker') {
          script {
            dockerImage = docker.build("parkinglot:${VERSION}")
          }
        }
      }
    }
    stage('Push Docker images to the Nexus repository') {
      when {
        expression {
          BRANCH_NAME == 'dev'
        }
      }
      steps {
        echo 'Pushing...'
        container('docker') {
          script {
            docker.withRegistry('https://nexus.tool.mddinternship.com/repository', 'nexus-jenkins') {
              dockerImage.push("${VERSION}")
            }
          }
        }
      }
    }
    stage('Set EKS Cluster') {
      when {
        expression {
          BRANCH_NAME == 'dev'
        }
      }
      steps {
        container('custom-ubuntu') {
          script {
            withAWS(credentials: 'mrosca-aws-key', region: 'eu-central-1') {
              sh '''
                aws eks update-kubeconfig --name internship-eks --region eu-central-1
                kubectl config set-context --current --namespace=parking-lot-ii
                kubectl get nodes 
              '''
              }     
            }
          }
        } 
      }   
    stage('Deploy application on AWS Kubernetes cluster') {
      when {
        expression {
          BRANCH_NAME == 'dev'
        }
      }
      steps {
        container('custom-ubuntu') {
          script {
            dir('helm') {
              git branch: 'main',
                  credentialsId: 'pl2-gitlab-helm',
                  url: 'https://gitlab.tool.mddinternship.com/parking-lot-2/parking-lot-ii-helm-chart.git'
              withAWS(credentials: 'mrosca-aws-key', region: 'eu-central-1') {
                sh '''
                  sed -i "s#^appVersion:.*#appVersion: ${VERSION}#" Chart.yaml
                  sed -i "s#^  tag:.*#  tag: ${VERSION}#" values.yaml
                  kubectl config set-context --current --namespace=parking-lot-ii
                  helm package .
                  helm upgrade -i --set PL_PGSQL_URL=${PL_PGSQL_URL} --set PL_PGSQL_DATABASE=${PL_PGSQL_DATABASE} --set PL_PGSQL_USERNAME=${PL_PGSQL_USERNAME} --set PL_PGSQL_PASSWORD=${PL_PGSQL_PASSWORD} parking-lot-ii parking-lot-ii-*.tgz
                '''                            
              }
            }
          }
        }
      }
    }
  }

  post {
    success {
      office365ConnectorSend webhookUrl: "${WEBHOOK}",
        message: "The Parking Lot 2 application has been deployed with version: ${VERSION}.",
        status: "Success"
    }
    failure {
      office365ConnectorSend webhookUrl: "${WEBHOOK}",
        message: "The deployment of Parking Lot 2 application version: ${VERSION} has failed.",
        status: "Failed"
    }
  }
}