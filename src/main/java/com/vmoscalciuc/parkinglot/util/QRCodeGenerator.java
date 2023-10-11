package com.vmoscalciuc.parkinglot.util;

import com.vmoscalciuc.parkinglot.model.ParkingLevelEntity;
import com.vmoscalciuc.parkinglot.model.ParkingLotEntity;
import com.vmoscalciuc.parkinglot.model.ParkingSpaceEntity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Component;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
public class QRCodeGenerator {

    private final int QR_CODE_SIZE = 200;
    public byte[] generateQRCode(ParkingSpaceEntity parkingSpaceEntity,
                                 ParkingLevelEntity levelEntity, ParkingLotEntity parkingLot) throws WriterException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        String qrCodeContent = parkingLot.getName()+ " LEVEL "+ levelEntity.getFloor()
                + " NUMBER: " + parkingSpaceEntity.getNumber();

        var qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeContent, BarcodeFormat.QR_CODE, QR_CODE_SIZE, QR_CODE_SIZE);

        try {
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        } catch (IOException e) {
            new RuntimeException("Cannot create QR code");
        }

        return outputStream.toByteArray();
    }
}
