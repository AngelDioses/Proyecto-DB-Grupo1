package Modelo.GenerarPDF;

import javax.swing.JTable;
import javax.swing.table.TableModel;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class PDFGeneral {

    public void generarPDF(JTable tablaCliente, String nomProy, String observac, String emplJefeProy, 
                           String ciaContrata, String codCliente, String annoIni, String annoFin, 
                           JTable tablaPartida, JTable tablaDProyPartidaMezcla) {
        try {
            // Obtener el modelo de las tablas
            TableModel modelCliente = tablaCliente.getModel();
            TableModel modelPartidas = tablaPartida.getModel();
            TableModel modelDProyPartidaMezcla = tablaDProyPartidaMezcla.getModel();

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
                .append("\\textbf{\\huge Presupuesto} \\\\ \n")
                .append("\\vspace{0.5cm}\n")
                .append("\\textbf{Proyecto:} ").append(nomProy).append(" \\\\ \n")
                .append("\\textbf{Descripción:} ").append(observac).append(" \\\\ \n")
                .append("\\textbf{Empleado:} ").append(emplJefeProy).append(" \\\\ \n")
                .append("\\textbf{Empresa Venta:} ").append(ciaContrata).append(" \\\\ \n")
                .append("\\textbf{Cliente:} ").append(codCliente).append(" \\\\ \n")
                .append("\\textbf{Fecha inicio:} ").append(annoIni).append(" \\\\ \n")
                .append("\\textbf{Fecha fin:} ").append(annoFin).append(" \\\\ \n")
                .append("\\end{center}\n")
                .append("\\vspace{1cm}\n")
                .append("\\begin{center}\n")
                .append("\\textbf{Partidas} \\\\ \n")
                .append("\\vspace{0.5cm}\n")
                .append("\\begin{tabular}{|p{4cm}|p{3cm}|p{3cm}|}\n")
                .append("\\hline\n")
                .append("\\textbf{Nombre de la Partida} & \\textbf{Tipo de Unidad} & \\textbf{Especificación de Unidad de Medida} \\\\\n")
                .append("\\hline\n");

            // Agregar filas de la tabla Partidas
            for (int i = 0; i < modelPartidas.getRowCount(); i++) {
                latexContent.append(modelPartidas.getValueAt(i, 0)).append(" & ")
                    .append(modelPartidas.getValueAt(i, 1)).append(" & ")
                    .append(modelPartidas.getValueAt(i, 2)).append(" \\\\\n")
                    .append("\\hline\n");
            }

            latexContent.append("\\end{tabular}\n")
                .append("\\vspace{1cm}\n")
                .append("\\textbf{Detalles de Proyecto, Partida y Mezcla} \\\\ \n")
                .append("\\vspace{0.5cm}\n")
                .append("\\begin{tabular}{|p{3cm}|p{3cm}|p{3cm}|}\n")
                .append("\\hline\n")
                .append("\\textbf{Importe} & \\textbf{IGV} & \\textbf{Total} \\\\\n")
                .append("\\hline\n");

            // Agregar filas de la tabla DProyPartidaMezcla
            for (int i = 0; i < modelDProyPartidaMezcla.getRowCount(); i++) {
                latexContent.append(modelDProyPartidaMezcla.getValueAt(i, 0)).append(" & ")
                    .append(modelDProyPartidaMezcla.getValueAt(i, 1)).append(" & ")
                    .append(modelDProyPartidaMezcla.getValueAt(i, 2)).append(" \\\\\n")
                    .append("\\hline\n");
            }

            latexContent.append("\\end{tabular}\n")
                .append("\\end{center}\n")
                .append("\\vspace{1cm}\n")
                .append("\\textbf{Resumen de Clientes} \\\\ \n")
                .append("\\vspace{0.5cm}\n")
                .append("\\begin{tabular}{|p{2cm}|p{6cm}|p{3cm}|p{2cm}|}\n")
                .append("\\hline\n")
                .append("\\textbf{CodClient} & \\textbf{DescPersona} & \\textbf{RUC} & \\textbf{Vigente} \\\\\n")
                .append("\\hline\n");

            // Agregar filas de la tabla Cliente
            for (int i = 0; i < modelCliente.getRowCount(); i++) {
                latexContent.append(modelCliente.getValueAt(i, 0)).append(" & ")
                    .append(modelCliente.getValueAt(i, 1)).append(" & ")
                    .append(modelCliente.getValueAt(i, 2)).append(" & ")
                    .append(modelCliente.getValueAt(i, 3)).append(" \\\\\n")
                    .append("\\hline\n");
            }

            latexContent.append("\\end{tabular}\n")
                .append("\\end{document}\n");

            // Escribir contenido LaTeX a un archivo .tex
            try (FileWriter fw = new FileWriter("presupuesto.tex")) {
                fw.write(latexContent.toString());
                System.out.println("Archivo .tex generado con éxito.");
            } catch (IOException e) {
                System.err.println("Error al escribir el archivo .tex: " + e.getMessage());
            }

            // Compilar report.tex a PDF usando pdflatex
            ProcessBuilder pb = new ProcessBuilder("C:\\Users\\angel\\AppData\\Local\\Programs\\MiKTeX\\miktex\\bin\\x64\\pdflatex.exe", "presupuesto.tex");
            pb.directory(new File(".")); // Directorio actual
            Process process = pb.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("PDF compilado con éxito.");
                // Mover el PDF generado
                Files.move(Paths.get("presupuesto.pdf"), Paths.get("C:\\Users\\angel\\Desktop\\REPORTES PDF\\Clientes\\presupuesto.pdf"), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("PDF movido a la ubicación de destino.");
            } else {
                System.err.println("Error al compilar el archivo LaTeX. Código de salida: " + exitCode);
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("Error en la generación de PDF: " + e.getMessage());
        }
    }
}