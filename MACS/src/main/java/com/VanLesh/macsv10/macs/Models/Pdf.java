package com.VanLesh.macsv10.macs.Models;

import android.os.Environment;
import android.util.Log;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;


/**
 * Created by samvanryssegem on 4/19/14.
 */
class Pdf {
    private static final Font redFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.RED);
    private static final Font subFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
    private static final Font smallBold = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);


    public static void maker(Calculation calc) {
        try {
            File pdfFile = new File(Environment.getExternalStorageDirectory().getPath() + "/Report.pdf");
            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(pdfFile));
            doc.open();
            addMetaData(doc, calc);
            addContent(doc, calc);
            doc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addMetaData(Document doc, Calculation calc) {
        doc.addTitle(calc.getTitle());
        doc.addAuthor(calc.getEngineerName());
        doc.addSubject(calc.getJobSite() + " This PDF was created using itext, and MACS is subject to" +
                                                                                            "the AGPL license");
        doc.addCreator("MACS v1.0 using itext");
        doc.addKeywords("MACS, itext, PDF");


    }

    private static void addContent(Document doc, Calculation calc) {
        try {
            //special font so we can render greek characters
            BaseFont bfComic = BaseFont.createFont("/system/fonts/DroidSansFallback.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font font1 = new Font(bfComic, 12);
            Font tgfont, tipfont;

            if (calc.getRollover() > calc.getDrag()) {
                tipfont = redFont;
                tgfont = smallBold;
            } else {
                tipfont = smallBold;
                tgfont = redFont;
            }
            Anchor anchor = new Anchor("Site", subFont);
            anchor.setName("Site Parameters");
            Paragraph identifier = new Paragraph("Calculation ID:    " + calc.getTitle(), font1);
            Paragraph engineer = new Paragraph("Engineer:    " + calc.getEngineerName(), font1);
            Paragraph jobsite = new Paragraph("Jobsite:     " + calc.getJobSite(), font1);
            Paragraph latitude = new Paragraph("Latitude:     " + calc.getLatitude(), font1);
            Paragraph longitude = new Paragraph("Longitude:     " + calc.getLongitude(), font1);

            Anchor vehicleAnchor = new Anchor("Vehicle", subFont);
            vehicleAnchor.setName("Vehicle");
            Paragraph vehicleclass = new Paragraph("Vehicle Class:     " + calc.getVehicle().getVehicleClass(), font1);
            Paragraph vehicletype = new Paragraph("Vehicle Type:     " + calc.getVehicle().getVehicleType(), font1);
            Paragraph vehicleweight = new Paragraph("Wv:     " + calc.getVehicle().getWv(), font1);
            Paragraph vehicletracklength = new Paragraph("Tl:     " + calc.getVehicle().getTrackL(), font1);
            Paragraph vehicletrackwidth = new Paragraph("Tw:     " + calc.getVehicle().getTrackW(), font1);
            Paragraph vehiclebladewidth = new Paragraph("Wb:     " + calc.getVehicle().getBladeW(), font1);

            Anchor soilAnchor = new Anchor("Soil", subFont);
            soilAnchor.setName("Soil");
            Paragraph soiltype = new Paragraph("Soil Type:     " + calc.getSoil().getName(), font1);
            Paragraph soilunitweight = new Paragraph("\u03B3:     " + calc.getSoil().getunitW(), font1);
            Paragraph soilfrictionangle = new Paragraph("\u03A6:     " + calc.getSoil().getfrictA(), font1);
            Paragraph soilcohesion = new Paragraph("c:     " + calc.getSoil().getC(), font1);

            Anchor measurementsAnchor = new Anchor("Measurements", subFont);
            anchor.setName("Measurements");
            Paragraph beta = new Paragraph("\u03B2:     " + calc.getBeta(), font1);
            Paragraph theta = new Paragraph("\u03B8:     " + calc.getTheta(), font1);
            Paragraph Ha = new Paragraph("Ha:     " + calc.getHa(), font1);
            Paragraph La = new Paragraph("La:     " + calc.getLa(), font1);
            Paragraph Db = new Paragraph("Db:     " + calc.getD_b(), font1);

            Anchor resultsAnchor = new Anchor("Results", subFont);
            int drag = (int) calc.getDrag();
            int roll = (int) calc.getRollover();
            Paragraph Tg = new Paragraph("Anchor cap Sliding :     " + drag, tgfont);
            Paragraph tipover = new Paragraph("Anchor cap Overturning:     " + roll, tipfont);

            doc.add(anchor);
            doc.add(identifier);
            doc.add(engineer);
            doc.add(jobsite);
            doc.add(latitude);
            doc.add(longitude);

            doc.add(vehicleAnchor);
            doc.add(vehicleclass);
            doc.add(vehicletype);
            doc.add(vehicleweight);
            doc.add(vehicletracklength);
            doc.add(vehicletrackwidth);
            doc.add(vehiclebladewidth);


            doc.add(soilAnchor);
            doc.add(soiltype);
            doc.add(soilunitweight);
            doc.add(soilfrictionangle);
            doc.add(soilcohesion);

            doc.add(measurementsAnchor);
            doc.add(beta);
            doc.add(theta);
            doc.add(Ha);
            doc.add(La);
            doc.add(Db);

            doc.add(resultsAnchor);
            doc.add(Tg);
            doc.add(tipover);
            Log.e("addingcontent", "complete");

        } catch (Exception e) {
            Font font1 = new Font(Font.FontFamily.HELVETICA, 24, Font.NORMAL, BaseColor.BLACK);

        }

    }


}
