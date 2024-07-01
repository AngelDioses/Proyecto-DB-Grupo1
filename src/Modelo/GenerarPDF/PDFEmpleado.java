
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
public class PDFEmpleado {
    public void generarPDF(JTable tablaEmpleado, String empresa, String ruc) {
        try {
            // Obtener el modelo de la tabla
            TableModel model = tablaEmpleado.getModel();

            // Generar el contenido LaTeX
            StringBuilder latexContent = new StringBuilder();
            latexContent.append("\\documentclass[a4paper, landscape]{article}\n");
            latexContent.append("\\usepackage[utf8]{inputenc}\n");
            latexContent.append("\\usepackage{longtable}\n"); // Para tablas largas
            latexContent.append("\\usepackage{geometry}\n");
            latexContent.append("\\geometry{top=2.5cm, bottom=2.5cm, left=2.5cm, right=2.5cm}\n"); // Ajustar márgenes
            latexContent.append("\\setlength{\\parindent}{0pt}\n"); // Eliminar sangría
            latexContent.append("\\begin{document}\n");
            latexContent.append("\\begin{center}\n");
            latexContent.append("\\textbf{\\Large Reporte de Empleados}\n");
            latexContent.append("\\end{center}\n");
            latexContent.append("\\vspace{0.5cm}\n");
            latexContent.append("\\textbf{\\Huge ").append(empresa).append("}\\\\ % Nombre de la empresa\n");
            latexContent.append("\\vspace{0.2cm}\n");
            latexContent.append("\\textbf{\\Large RUC: ").append(ruc).append("}\\\\ % RUC\n");
            latexContent.append("\\vspace{0.5cm}\n");
            latexContent.append("\\begin{longtable}{|c|c|c|c|c|c|c|c|}\n");
            latexContent.append("\\hline\n");
            latexContent.append("CodPersona & DescPersona & DNI & NroCIP & FecNac & LicCond & Celular & Vigente \\\\\n");
            latexContent.append("\\hline\n");

            for (int i = 0; i < model.getRowCount(); i++) {
                latexContent.append(model.getValueAt(i, 0)).append(" & ");
                latexContent.append(model.getValueAt(i, 1)).append(" & ");
                latexContent.append(model.getValueAt(i, 2)).append(" & ");
                latexContent.append(model.getValueAt(i, 3)).append(" & ");
                latexContent.append(model.getValueAt(i, 4)).append(" & ");
                latexContent.append(model.getValueAt(i, 5)).append(" & ");
                latexContent.append(model.getValueAt(i, 6)).append(" & ");
                latexContent.append(model.getValueAt(i, 7)).append(" \\\\\n");
            }

            latexContent.append("\\hline\n");
            latexContent.append("\\end{longtable}\n");
            latexContent.append("\\end{document}\n");

            // Escribir contenido LaTeX a un archivo .tex
            try (FileWriter fw = new FileWriter("reporteEmpleado.tex")) {
                fw.write(latexContent.toString());
                System.out.println("Archivo .tex generado con éxito.");
            } catch (IOException e) {
                System.err.println("Error al escribir el archivo .tex: " + e.getMessage());
            }

            // Compilar report.tex a PDF usando pdflatex
            ProcessBuilder pb = new ProcessBuilder("C:\\Users\\angel\\AppData\\Local\\Programs\\MiKTeX\\miktex\\bin\\x64\\pdflatex.exe", "reporteEmpleado.tex");
            pb.directory(new File(".")); // Directorio actual
            Process process = pb.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("PDF compilado con éxito.");
                // Mover el PDF generado
                Files.move(Paths.get("reporteEmpleado.pdf"), Paths.get("C:\\Users\\angel\\Desktop\\REPORTES PDF\\Empleados\\reporteEmpleado.pdf"), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("PDF movido a la ubicación de destino.");
            } else {
                System.err.println("Error al compilar el archivo LaTeX. Código de salida: " + exitCode);
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("Error en la generación de PDF: " + e.getMessage());
        }
    }
}
