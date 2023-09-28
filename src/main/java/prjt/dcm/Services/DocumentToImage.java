package prjt.dcm.Services;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTheme;
import org.springframework.stereotype.Service;
import org.apache.commons.codec.binary.Base64;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

@Service
public class DocumentToImage {
    public String convertPdfToImage(String pdfUrl) throws IOException {
        URL url = new URL(pdfUrl);
        try (InputStream inputStream = url.openStream()) {
            // Charger contenu du pdf
            PDDocument document = PDDocument.load(inputStream);
            try {
                //Rendre les pages = images
                PDFRenderer pdfRenderer = new PDFRenderer(document);
                //Page 1
                PDPage page = document.getPage(0);
                BufferedImage image = pdfRenderer.renderImageWithDPI(0, 300);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(image, "PNG", baos);

                byte[] imageBytes = baos.toByteArray();
                return Base64.encodeBase64String(imageBytes);
            } finally {
                document.close();
            }
        }
    }
    public String convertPptxToImage(String pptxUrl) throws IOException {
        URL url = new URL(pptxUrl);
        try (InputStream inputStream = url.openStream()) {
            XMLSlideShow ppt = new XMLSlideShow(inputStream);
            try {
                List<XSLFSlide> slideList = ppt.getSlides();
                XSLFSlide[] slides = slideList.toArray(new XSLFSlide[0]);
                if(slides.length<1)
                {
                    return  null;
                }
                XSLFSlide slide = slides[0];

                Dimension pageSize = ppt.getPageSize();
                int width = (int) pageSize.getWidth();
                int height = (int) pageSize.getHeight();

                BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = image.createGraphics();
                slide.draw(graphics);
                graphics.dispose();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(image, "PNG", baos);

                // Convertir l'image en tableau de bytes
                byte[] imageBytes = baos.toByteArray();

                // Convertir le tableau de bytes en base64
                String base64Image = Base64.encodeBase64String(imageBytes);

                return base64Image;
            } finally {
                ppt.close(); // Fermez le diaporama après utilisation
            }
        }
    }
}