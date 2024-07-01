package Modelo.GenerarPDF;

import javax.swing.JTable;
import javax.swing.table.TableModel;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 *
 * @author angel
 */
public class PDFCliente {

    public void generarPDF(JTable tablaCliente, String empresa, String ruc) {
        try {
            // Obtener el modelo de la tabla
            TableModel model = tablaCliente.getModel();

            // Generar el contenido LaTeX
            StringBuilder latexContent = new StringBuilder();
            latexContent.append("\\documentclass{article}\n")
                .append("\\usepackage[utf8]{inputenc}\n")
                .append("\\usepackage{graphicx}\n")
                .append("\\usepackage{array}\n")
                .append("\\usepackage{geometry}\n")
                .append("\\geometry{left=2.5cm, right=2.5cm, top=2.5cm, bottom=2.5cm}\n")
                .append("\\setlength{\\parindent}{0pt}\n")
                .append("\\begin{document}\n")
                .append("\\begin{center}\n")
                .append("\\vspace{0.5cm}\n")
                .append("\\textbf{\\Huge ").append(empresa).append("}\\\\ % Nombre de la empresa\n")
                .append("\\vspace{0.2cm}\n")
                .append("\\textbf{\\Large RUC: ").append(ruc).append("}\\\\ % RUC\n")
                .append("\\vspace{1cm}\n")
                .append("\\textbf{\\Large Reporte de Clientes}\n")
                .append("\\end{center}\n")
                .append("\\vspace{1cm}\n")
                .append("\\begin{center}\n")
                .append("\\begin{tabular}{|c|c|c|c|}\n")
                .append("\\hline\n")
                .append("CodClient & DescPersona & RUC & Vigente \\\\\n")
                .append("\\hline\n");
            

            // Agregar filas a la tabla
            for (int i = 0; i < model.getRowCount(); i++) {
                latexContent.append(model.getValueAt(i, 0)).append(" & ")
                    .append(model.getValueAt(i, 1)).append(" & ")
                    .append(model.getValueAt(i, 2)).append(" & ")
                    .append(model.getValueAt(i, 3)).append(" \\\\\n");
            }

            latexContent.append("\\hline\n");
            latexContent.append("\\end{tabular}\n");
            latexContent.append("\\end{center}\n");
            latexContent.append("\\end{document}\n");

            // Escribir contenido LaTeX a un archivo .tex
            try (FileWriter fw = new FileWriter("reporteCliente.tex")) {
                fw.write(latexContent.toString());
                System.out.println("Archivo .tex generado con éxito.");
            } catch (IOException e) {
                System.err.println("Error al escribir el archivo .tex: " + e.getMessage());
            }

            // Compilar report.tex a PDF usando pdflatex
            ProcessBuilder pb = new ProcessBuilder("C:\\Users\\angel\\AppData\\Local\\Programs\\MiKTeX\\miktex\\bin\\x64\\pdflatex.exe", "reporteCliente.tex");
            pb.directory(new File(".")); // Directorio actual
            Process process = pb.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("PDF compilado con éxito.");
                // Mover el PDF generado
                Files.move(Paths.get("reporteCliente.pdf"), Paths.get("C:\\Users\\angel\\Desktop\\REPORTES PDF\\Clientes\\reporteCliente.pdf"), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("PDF movido a la ubicación de destino.");
            } else {
                System.err.println("Error al compilar el archivo LaTeX. Código de salida: " + exitCode);
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("Error en la generación de PDF: " + e.getMessage());
        }
    }
}
