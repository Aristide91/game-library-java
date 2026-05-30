package util;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import model.GamePurchase;
import model.User;

import java.awt.Color;
import java.io.FileOutputStream;

public class PdfService implements Exportable {

    @Override
    public void export(User user) {
        generatePdf(user);
    }

    public static void generatePdf(User user) {
        String filename = user.getNickname() + "_bibliotheque.pdf";
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(filename));
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18,
                    Color.BLUE);
            Paragraph title =
                    new Paragraph("Bibliothèque de " + user.getNickname(),
                            titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);

            addTableHeader(table, "Jeu");
            addTableHeader(table, "Acheté le");
            addTableHeader(table, "Temps (h)");
            addTableHeader(table, "Statut");

            for (GamePurchase p : user.getLibrary().getPurchases()) {
                table.addCell(p.getGame().getTitle());
                table.addCell(p.getPurchaseDate().toString());
                table.addCell(String.valueOf(p.getPlayTime()));

                if (p.isRefunded()) {
                    PdfPCell cell = new PdfPCell(new Phrase("REMBOURSÉ"));
                    cell.setBackgroundColor(Color.RED);
                    table.addCell(cell);
                } else {
                    table.addCell("Actif");
                }
            }

            document.add(table);

            Paragraph footer =
                    new Paragraph("\nFichier utilisateur",
                            FontFactory.getFont(FontFactory.COURIER, 10,
                                    Color.GRAY));
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();
            System.out.println("PDF généré avec succès : " + filename);
            
            try {
                java.io.File file = new java.io.File(filename);
                if (file.exists() && java.awt.Desktop.isDesktopSupported()) {
                    java.awt.Desktop.getDesktop().open(file);
                }
            } catch (Exception ex) {
                System.out.println("Impossible d'ouvrir le PDF automatiquement.");
            }

        } catch (Exception e) {
            AppLogger.logError("Erreur PDF", e);
            System.out.println("Erreur lors de la création du PDF.");
        }
    }

    private static void addTableHeader(PdfPTable table, String headerTitle) {
        PdfPCell header = new PdfPCell();
        header.setBackgroundColor(Color.LIGHT_GRAY);
        header.setBorderWidth(2);
        header.setPhrase(new Phrase(headerTitle));
        table.addCell(header);
    }
}