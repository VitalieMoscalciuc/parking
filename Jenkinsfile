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
          - name: postgres
            image: postgres:15.3
            tty: true
            env:
              - name: POSTGRES_PASSWORD
                value: postgres
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
  }

  stages {
    stage('SonarQube Code Verification'){
      when {
        expression {
          BRANCH_NAME == 'dev' || BRANCH_NAME == 'main'
        }
      }
      steps {
        container('custom-ubuntu') {
          script {
            withSonarQubeEnv(credentialsId: 'sonarqube-tl-token', installationName: 'SonarQube') {
            sh 'mvn clean install sonar:sonar'
            }
          }
        }
      }
    }

    stage('Extract application version') {
      when {
        expression {
          BRANCH_NAME == 'dev'
        }
      }
      steps {
        script {
          sh '''awk -F"[<>]" '/<version>[0-9]+\\.[0-9]+\\.[0-9]+-SNAPSHOT<\\/version>/{gsub("-SNAPSHOT", "", $3); print $3; exit}' pom.xml > ./temp.txt'''
          env.VERSION = readFile('./temp.txt').trim()
        }
      }
    }

    stage('Set Data Base Variables') {
      when {
        expression {
          BRANCH_NAME == 'dev'
        }
      }
      steps {
        sh(script: '''
          sed -i "s#^spring.datasource.url=.*#spring.datasource.url=${PL_PGSQL_URL}/${PL_PGSQL_DATABASE}#" src/main/resources/application.properties
          sed -i "s#^spring.datasource.username=.*#spring.datasource.username=${PL_PGSQL_USERNAME}#" src/main/resources/application.properties
          sed -i "s#^spring.datasource.password=.*#spring.datasource.password=${PL_PGSQL_PASSWORD}#" src/main/resources/application.properties
          ''')
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
              dockerImage.push('latest')
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
                  sed -i "s#^  tag:.*#tag: ${VERSION}#" values.yaml
                  kubectl config set-context --current --namespace=parking-lot-ii
                  kubectl get namespaces
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
    always {
      echo 'post always'
    }
    success {
      script {
        echo "The Parking Lot 2 application has been deployed with version: ${VERSION}."
      }
    }
    failure {
      echo 'post failure'
    }
  }
}

