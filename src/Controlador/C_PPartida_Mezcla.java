package Controlador;

import Modelo.DAO.PPartidaDAO;
import static Vistas.V_Login.varCodCiaGlobalDeLogin;
import Modelo.Message.Mensaje1;
import Modelo.Message.Mensaje2;
import Modelo.DAO.PPartida_MezclaDAO;
import Modelo.PPartida;
import Modelo.PPartida_Mezcla;
import Vistas.V_PPartida_Mezcla;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import Custom_by_me.SelectPartidas;

@SuppressWarnings("unchecked")
public class C_PPartida_Mezcla implements ItemListener, ActionListener, KeyListener, MouseListener {
    PPartida_MezclaDAO pmDAO = new PPartida_MezclaDAO();
    V_PPartida_Mezcla vpm = new V_PPartida_Mezcla();
    DefaultTableModel modelPartidaMezclaI = new DefaultTableModel();
    DefaultTableModel modelPartidaMezclaE = new DefaultTableModel();
    TableRowSorter<DefaultTableModel> sorterI;
    TableRowSorter<DefaultTableModel> sorterE;
    int ordenI, ordenE;
    List<PPartida> partidas_E;
    List<PPartida> partidas_I;
    List<PPartida_Mezcla> partidasMezcla_E;
    List<PPartida_Mezcla> partidasMezcla_I;

    public C_PPartida_Mezcla(V_PPartida_Mezcla vpm) {
        this.vpm = vpm;
        this.vpm.btt_Actualizar_I.addActionListener(this);
        this.vpm.btt_Registrar_I.addActionListener(this);
        this.vpm.btt_Eliminar_I.addActionListener(this);
        this.vpm.bttn_confI.addActionListener(this);
        this.vpm.tablaPartida_Mezcla_I.addMouseListener(this);
        this.vpm.padCodPartida_I.addItemListener(this);

        this.vpm.btt_Actualizar_E.addActionListener(this);
        this.vpm.btt_Registrar_E.addActionListener(this);
        this.vpm.btt_Eliminar_E.addActionListener(this);
        this.vpm.tablaPartida_Mezcla_E.addMouseListener(this);
        this.vpm.padCodPartida_E.addItemListener(this);
        this.vpm.bttn_confE.addActionListener(this);

        this.vpm.actualizaTabla.addActionListener(this);
        this.vpm.nuevo.addActionListener(this);

        this.partidas_E = new PPartidaDAO().listarPorCodCia(varCodCiaGlobalDeLogin, "E");
        this.partidas_I = new PPartidaDAO().listarPorCodCia(varCodCiaGlobalDeLogin, "I");
        init();
    }

    public void init() {
        initTablaPartidaMezcla_I();
        initTablaPartidaMezcla_E();
        vpm.init();
        initListarPartidas();
    }

    public void initListarPartidas() {
        SwingUtilities.invokeLater(()->{
            vpm.codPartida_I.removeAllItems();
            vpm.padCodPartida_I.removeAllItems();
            vpm.codPartida_E.removeAllItems();
            vpm.padCodPartida_E.removeAllItems();
            List<PPartida> listaParI = new PPartidaDAO().listarPorCodCia(varCodCiaGlobalDeLogin, "I");
            List<PPartida> listaParE = new PPartidaDAO().listarPorCodCia(varCodCiaGlobalDeLogin, "E");
            List<PPartida_Mezcla> listaParIP = new PPartida_MezclaDAO().listarCodPartida_Mezcla(varCodCiaGlobalDeLogin, "I");
            List<PPartida_Mezcla> listaParEP = new PPartida_MezclaDAO().listarCodPartida_Mezcla(varCodCiaGlobalDeLogin, "E");
            vpm.padCodPartida_I.addItem(0);
            vpm.padCodPartida_E.addItem(0);
    
            for (int i = 0; i < listaParIP.size(); i++) {
                vpm.padCodPartida_I.addItem(listaParIP.get(i).getCodPartida());
            }
    
            for (int i = 0; i < listaParI.size(); i++) {
                String descripcion = listaParI.get(i).getDesPartida();
                int codPartida = listaParI.get(i).getCodPartida();
                Object item = new SelectPartidas(descripcion, String.valueOf(codPartida));
                vpm.codPartida_I.addItem(item);
                // vpm.codPartida_I.addItem(listaParI.get(i).getCodPartida());
            }
    
            for (int i = 0; i < listaParEP.size(); i++) {
                vpm.padCodPartida_E.addItem(listaParEP.get(i).getCodPartida());
            }
    
            for (int i = 0; i < listaParE.size(); i++) {
                String descripcion = listaParE.get(i).getDesPartida();
                int codPartida = listaParE.get(i).getCodPartida();
                Object item = new SelectPartidas(descripcion, String.valueOf(codPartida));
                vpm.codPartida_E.addItem(item);
                // vpm.codPartida_E.addItem(listaParE.get(i).getCodPartida());
            }



        });

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("DENTRO DE ACTION PARTIDA_MEZCLA");
        if (e.getSource() == vpm.btt_Registrar_I) {
            registrarDatos("I");
            actualizarTabla();
        }
        if (e.getSource() == vpm.btt_Actualizar_I) {
            actualizarDatos("I");
            actualizarTabla();
        }
        if (e.getSource() == vpm.btt_Eliminar_I) {
            eliminarDatos("I");
            actualizarTabla();
        }
        if (e.getSource() == vpm.bttn_confI) {
            vpm.orden_I.setEnabled(true);
            vpm.nivel_I.setEnabled(true);
        }
        if (e.getSource() == vpm.btt_Registrar_E) {
            registrarDatos("E");
            actualizarTabla();
        }
        if (e.getSource() == vpm.btt_Actualizar_E) {
            actualizarDatos("E");
            actualizarTabla();
        }
        if (e.getSource() == vpm.btt_Eliminar_E) {
            eliminarDatos("E");
            actualizarTabla();
        }
        if (e.getSource() == vpm.bttn_confE) {
            vpm.orden_E.setEnabled(true);
            vpm.nivel_I.setEnabled(true);
        }
        if (e.getSource() == vpm.actualizaTabla) {
            actualizarTabla();
        }
        if (e.getSource() == vpm.nuevo) {
            vaciarCampos();
            actualizarTabla();
        }
    }

    public void actualizarTabla() {
        limpiarTabla(modelPartidaMezclaI);
        limpiarTabla(modelPartidaMezclaE);
        initTablaPartidaMezcla_I();
        initTablaPartidaMezcla_E();
        // initListarPartidas(); //Para una mejor frescura visual.
        // Me da un error
        System.out.println("Refrescando tabla automaticamente.");
    }

    public void initTablaPartidaMezcla_I() {
        List<PPartida_Mezcla> listaI = new PPartida_MezclaDAO().listarPorCodCia(varCodCiaGlobalDeLogin, "I");
        modelPartidaMezclaI = (DefaultTableModel) vpm.tablaPartida_Mezcla_I.getModel();
        Object[] o = new Object[10];
        sorterI = new TableRowSorter<>(modelPartidaMezclaI);
        vpm.tablaPartida_Mezcla_I.setRowSorter(sorterI);
        limpiarTabla(modelPartidaMezclaI);
        for (int i = 0; i < listaI.size(); i++) {
            o[0] = listaI.get(i).getCorr();
            int codPartida = listaI.get(i).getCodPartida();
            this.partidas_I.forEach(partida -> {
                if (partida.getCodPartida() == codPartida) {
                    o[2] = partida.getDesPartida();
                }
            });
            o[1] = codPartida;
            o[3] = (listaI.get(i).getPadCodPartida()) == 0 ? "NULL" : listaI.get(i).getPadCodPartida();
            o[4] = listaI.get(i).getNivel();
            o[5] = listaI.get(i).getOrden();
            o[6] = listaI.get(i).gettUnitMed();
            o[7] = listaI.get(i).geteUnitMed();
            o[8] = listaI.get(i).getCostoUnit();
            o[9] = (listaI.get(i).getVigente()) == '1' ? "Si" : "No";
            modelPartidaMezclaI.addRow(o);
        }
        vpm.tablaPartida_Mezcla_I.setModel(modelPartidaMezclaI);
    }

    public void initTablaPartidaMezcla_E() {
        List<PPartida_Mezcla> listaE = new PPartida_MezclaDAO().listarPorCodCia(varCodCiaGlobalDeLogin, "E");
        modelPartidaMezclaE = (DefaultTableModel) vpm.tablaPartida_Mezcla_E.getModel();
        Object[] o = new Object[10];
        sorterE = new TableRowSorter<>(modelPartidaMezclaE);
        vpm.tablaPartida_Mezcla_E.setRowSorter(sorterE);
        limpiarTabla(modelPartidaMezclaE);
        for (int i = 0; i < listaE.size(); i++) {
            o[0] = listaE.get(i).getCorr();
            int codPartida = listaE.get(i).getCodPartida();
            this.partidas_E.forEach(partida -> {
                if (partida.getCodPartida() == codPartida) {
                    o[2] = partida.getDesPartida();
                }
            });
            o[1] = listaE.get(i).getCodPartida();
            o[3] = (listaE.get(i).getPadCodPartida()) == 0 ? "NULL" : listaE.get(i).getPadCodPartida();
            o[4] = listaE.get(i).getNivel();
            o[5] = listaE.get(i).getOrden();
            o[6] = listaE.get(i).gettUnitMed();
            o[7] = listaE.get(i).geteUnitMed();
            o[8] = listaE.get(i).getCostoUnit();
            o[9] = (listaE.get(i).getVigente()) == '1' ? "Si" : "No";
            modelPartidaMezclaE.addRow(o);
        }
        vpm.tablaPartida_Mezcla_E.setModel(modelPartidaMezclaE);
    }

    public void limpiarTabla(DefaultTableModel model) {
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        return;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        return;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        return;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int fila, cor, cod;
        if (e.getSource() == vpm.tablaPartida_Mezcla_I) {
            fila = vpm.tablaPartida_Mezcla_I.getSelectedRow();
            cor = Integer.parseInt(vpm.tablaPartida_Mezcla_I.getValueAt(fila, 0).toString());
            cod = Integer.parseInt(vpm.tablaPartida_Mezcla_I.getValueAt(fila, 1).toString());
            System.out.println("PartidaMezcla = " + cor + " " + cod);
            PPartida_Mezcla pmI = new PPartida_MezclaDAO().listarId(varCodCiaGlobalDeLogin, "I", cod, cor);

            int partidaID = pmI.getCodPartida();
            this.partidas_I.forEach(partida -> {
                if (partida.getCodPartida() == partidaID) {
                    vpm.codPartida_I.setSelectedItem(
                            new SelectPartidas(partida.getDesPartida(), String.valueOf(partida.getCodPartida())));
                }
            });
            // vpm.codPartida_I.setSelectedItem(pmI.getCodPartida());

            vpm.costoUnit_I.setValue(pmI.getCostoUnit());
            if (pmI.getPadCodPartida() == 0)
                vpm.padCodPartida_I.setSelectedItem(0);
            else
                vpm.padCodPartida_I.setSelectedItem(pmI.getPadCodPartida());
            vpm.nivel_I.setValue(pmI.getNivel());
            vpm.nivel_I.setEnabled(true);
            vpm.orden_I.setValue(pmI.getOrden());

            ordenI = pmI.getOrden();

            vpm.orden_I.setEnabled(true);
            vpm.vigente_I.setSelectedItem((pmI.getVigente() == '1' ? "Vigente" : "No Vigente"));
        }

        if (e.getSource() == vpm.tablaPartida_Mezcla_E) {
            fila = vpm.tablaPartida_Mezcla_E.getSelectedRow();
            cor = Integer.parseInt(vpm.tablaPartida_Mezcla_E.getValueAt(fila, 0).toString());
            cod = Integer.parseInt(vpm.tablaPartida_Mezcla_E.getValueAt(fila, 1).toString());
            System.out.println("PartidaMezcla = " + cor);
            PPartida_Mezcla pmE = new PPartida_MezclaDAO().listarId(varCodCiaGlobalDeLogin, "E", cod, cor);

            int partidaID = pmE.getCodPartida();
            this.partidas_E.forEach(partida -> {
                if (partida.getCodPartida() == partidaID) {
                    vpm.codPartida_E.setSelectedItem(
                            new SelectPartidas(partida.getDesPartida(), String.valueOf(partida.getCodPartida())));
                }
            });
            // vpm.codPartida_E.setSelectedItem(pmE.getCodPartida());

            vpm.costoUnit_E.setValue(pmE.getCostoUnit());
            if (pmE.getPadCodPartida() == 0)
                vpm.padCodPartida_E.setSelectedItem(0);
            else
                vpm.padCodPartida_E.setSelectedItem(pmE.getPadCodPartida());
            vpm.nivel_E.setValue(pmE.getNivel());
            vpm.nivel_E.setEnabled(true);
            vpm.orden_E.setValue(pmE.getOrden());

            ordenE = pmE.getOrden();

            vpm.orden_E.setEnabled(true);
            vpm.vigente_E.setSelectedItem((pmE.getVigente() == '1' ? "Vigente" : "No Vigente"));
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        return;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        return;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        return;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        return;
    }

    public void registrarDatos(String tip) {
        PPartida_Mezcla pm = new PPartida_Mezcla();
        char vig;
        if (tip == "I") {
            pm.setCodCia(varCodCiaGlobalDeLogin);
            pm.setIngEgr(tip);

            Object item = vpm.codPartida_I.getSelectedItem();
            int codPartida = Integer.parseInt(((SelectPartidas) item).getValue());
            pm.setCodPartida(codPartida);
            // pm.setCodPartida(Integer.parseInt(vpm.codPartida_I.getSelectedItem().toString()));

            pm.setPadCodPartida(Integer.parseInt(vpm.padCodPartida_I.getSelectedItem().toString()));
            pm.setNivel(Integer.parseInt(vpm.nivel_I.getValue().toString()));
            pm.setOrden(Integer.parseInt(vpm.orden_I.getValue().toString()));
            pm.setCostoUnit(Float.parseFloat(vpm.costoUnit_I.getValue().toString()));

            PPartida p = new PPartidaDAO().listarId(varCodCiaGlobalDeLogin, pm.getCodPartida(), "I");
            pm.settUnitMed(p.gettUniMed());
            pm.seteUnitMed(p.geteUniMed());

            Object selectedItem2 = vpm.vigente_I.getSelectedItem();
            if (selectedItem2 == null) {
                vig = '0'; // Assuming '0' as default for non-vigente
            } else {
                String selectedString = selectedItem2.toString();
                vig = "Vigente".equals(selectedString) ? '1' : '0';
            }
            pm.setVigente(vig);
        } else {
            pm.setCodCia(varCodCiaGlobalDeLogin);
            pm.setIngEgr(tip);

            Object item = vpm.codPartida_E.getSelectedItem();
            int codPartida = Integer.parseInt(((SelectPartidas) item).getValue());
            pm.setCodPartida(codPartida);
            // pm.setCodPartida(Integer.parseInt(vpm.codPartida_E.getSelectedItem().toString()));

            pm.setPadCodPartida(Integer.parseInt(vpm.padCodPartida_E.getSelectedItem().toString()));
            pm.setNivel(Integer.parseInt(vpm.nivel_E.getValue().toString()));
            pm.setOrden(Integer.parseInt(vpm.orden_E.getValue().toString()));
            pm.setCostoUnit(Float.parseFloat(vpm.costoUnit_E.getValue().toString()));

            PPartida p = new PPartidaDAO().listarId(varCodCiaGlobalDeLogin, pm.getCodPartida(), "E");
            pm.settUnitMed(p.gettUniMed());
            pm.seteUnitMed(p.geteUniMed());

            // Check if selected item is null or empty before using it
            Object selectedItem = vpm.vigente_E.getSelectedItem();
            if (selectedItem == null) {
                // Handle the null case, e.g., set a default value or show an error message
                vig = '0'; // Assuming '0' as default for non-vigente
            } else {
                // Safely convert to string and perform the comparison
                String selectedString = selectedItem.toString();
                vig = "Vigente".equals(selectedString) ? '1' : '0';
            }
            pm.setVigente(vig);
        }
        if (pmDAO.add(pm) == 1) {
            showMessage2("Partida_Mezcla registrada correctamente");
            vaciarCampos();
        } else {
            showMessage1("Error al registrar el Partida_Mezcla ");
        }
    }

    private boolean showMessage1(String message) {
        Mensaje1 obj = new Mensaje1(Frame.getFrames()[1], true);
        obj.showMessage(message);
        return obj.isAceptar();
    }

    private boolean showMessage2(String message) {
        Mensaje2 obj = new Mensaje2(Frame.getFrames()[1], true);
        obj.showMessage(message);
        return obj.isAceptar();
    }

    public void actualizarDatos(String tip) {
        int fila, cod, cor, pad;
        char vig;
        PPartida_Mezcla pm = new PPartida_Mezcla();
        if (tip == "I") {
            fila = vpm.tablaPartida_Mezcla_I.getSelectedRow();
            if (fila != -1) {
                System.out.println("Hay filas seleccionadas.");
                cor = Integer.parseInt(vpm.tablaPartida_Mezcla_I.getValueAt(fila, 0).toString());
                cod = Integer.parseInt(vpm.tablaPartida_Mezcla_I.getValueAt(fila, 1).toString());
                if ((vpm.tablaPartida_Mezcla_I.getValueAt(fila,3).toString()) == "NULL") {
                    pad = 0;
                } else {
                    pad = Integer.parseInt(vpm.tablaPartida_Mezcla_I.getValueAt(fila, 3).toString());
                }
                pm.setCodCia(varCodCiaGlobalDeLogin);
                pm.setIngEgr(tip);
                pm.setCodPartida(cod);
                pm.setCorr(cor);
                pm.setNivel(Integer.parseInt(vpm.nivel_I.getValue().toString()));
                pm.setOrden(Integer.parseInt(vpm.orden_I.getValue().toString()));
                pm.setCostoUnit(Float.parseFloat(vpm.costoUnit_I.getValue().toString()));

                Object selectedItem2 = vpm.vigente_I.getSelectedItem();
                if (selectedItem2 == null) {
                    vig = '0'; // Assuming '0' as default for non-vigente
                } else {
                    String selectedString = selectedItem2.toString();
                    vig = "Vigente".equals(selectedString) ? '1' : '0';
                }
                pm.setVigente(vig);

                if (pmDAO.busOrden(varCodCiaGlobalDeLogin, tip, pad, pm.getNivel(), pm.getOrden()) == true) {
                    PPartida_Mezcla pmI = new PPartida_MezclaDAO().listarOrden(varCodCiaGlobalDeLogin, "I", pad,
                            pm.getNivel(), pm.getOrden());
                    pmI.setOrden(ordenI);
                    pmDAO.actualizar(pmI);
                }
                if (pmDAO.actualizar(pm) == 1) {
                    showMessage2("Partida_Mezcla actualizada correctamente");
                    vaciarCampos();
                } else {
                    showMessage1("Error al actualizar Partida_Mezcla");
                }
            } else {
                showMessage1("Debe seleccionar una fila");
            }
        } else {
            fila = vpm.tablaPartida_Mezcla_E.getSelectedRow();
            if (fila != -1) {
                System.out.println("Hay filas seleccionadas.");
                cor = Integer.parseInt(vpm.tablaPartida_Mezcla_E.getValueAt(fila, 0).toString());
                cod = Integer.parseInt(vpm.tablaPartida_Mezcla_E.getValueAt(fila, 1).toString());
                if ((vpm.tablaPartida_Mezcla_E.getValueAt(fila, 3).toString()) == "NULL") {
                    pad = 0;
                } else {
                    pad = Integer.parseInt(vpm.tablaPartida_Mezcla_E.getValueAt(fila, 3).toString());
                }
                pm.setCodCia(varCodCiaGlobalDeLogin);
                pm.setIngEgr(tip);
                pm.setCodPartida(cod);
                pm.setCorr(cor);
                pm.setNivel(Integer.parseInt(vpm.nivel_E.getValue().toString()));
                pm.setOrden(Integer.parseInt(vpm.orden_E.getValue().toString()));
                pm.setCostoUnit(Float.parseFloat(vpm.costoUnit_E.getValue().toString()));
                System.out.println(pm.getCostoUnit());

                Object selectedItem2 = vpm.vigente_E.getSelectedItem();
                if (selectedItem2 == null) {
                    vig = '0'; // Assuming '0' as default for non-vigente
                } else {
                    String selectedString = selectedItem2.toString();
                    vig = "Vigente".equals(selectedString) ? '1' : '0';
                }

                pm.setVigente(vig);
                if (pmDAO.busOrden(varCodCiaGlobalDeLogin, tip, pad, pm.getNivel(), pm.getOrden()) == true) {
                    PPartida_Mezcla pmE = new PPartida_MezclaDAO().listarOrden(varCodCiaGlobalDeLogin, "E", pad,
                            pm.getNivel(), pm.getOrden());
                    pmE.setOrden(ordenE);
                    pmDAO.actualizar(pmE);
                }
                if (pmDAO.actualizar(pm) == 1) {
                    showMessage2("Partida_Mezcla actualizada correctamente");
                    vaciarCampos();
                } else {
                    showMessage1("Error al actualizar Partida_Mezcla");
                }
            } else {
                showMessage1("Debe seleccionar una fila");
            }
        }
    }

    public void eliminarDatos(String tip) {
        int fila, cod, cor;
        if (tip == "I") {
            fila = vpm.tablaPartida_Mezcla_I.getSelectedRow();
            System.out.println("La fila es" + fila);
            if (fila != -1) {
                System.out.println("Hay filas seleccionadas.");
                cor = Integer.parseInt(vpm.tablaPartida_Mezcla_I.getValueAt(fila, 0).toString());
                cod = Integer.parseInt(vpm.tablaPartida_Mezcla_I.getValueAt(fila, 1).toString());
                new PPartida_MezclaDAO().eliminarDatos(varCodCiaGlobalDeLogin, cod, tip, cor);
            } else {
                showMessage1("Debe seleccionar una fila");
            }
        } else {
            fila = vpm.tablaPartida_Mezcla_E.getSelectedRow();
            System.out.println("La fila es" + fila);
            if (fila != -1) {
                System.out.println("Hay filas seleccionadas.");
                cor = Integer.parseInt(vpm.tablaPartida_Mezcla_E.getValueAt(fila, 0).toString());
                cod = Integer.parseInt(vpm.tablaPartida_Mezcla_E.getValueAt(fila, 1).toString());
                new PPartida_MezclaDAO().eliminarDatos(varCodCiaGlobalDeLogin, cod, tip, cor);
            } else {
                showMessage1("Debe seleccionar una fila");
            }
        }
    }

    public void vaciarCampos() {
        initListarPartidas();
        vpm.nivel_E.setValue(0);
        vpm.nivel_I.setValue(0);
        vpm.orden_E.setValue(0);
        vpm.orden_I.setValue(0);
        vpm.costoUnit_E.setValue(0);
        vpm.costoUnit_I.setValue(0);
        if (vpm.codPartida_I.getItemCount() > 0) {
            vpm.codPartida_I.setSelectedIndex(0);
        }
        if (vpm.codPartida_E.getItemCount() > 0) {
            vpm.codPartida_E.setSelectedIndex(0);
        }
        if (vpm.padCodPartida_I.getItemCount() > 0) {
            vpm.padCodPartida_I.setSelectedIndex(0);
        }
        if (vpm.padCodPartida_E.getItemCount() > 0) {
            vpm.padCodPartida_E.setSelectedIndex(0);
        }

        vpm.nivel_I.setModel(new SpinnerNumberModel(1, 1, 99, 1));
        vpm.orden_I.setModel(new SpinnerNumberModel(1, 1, 99, 1));
        vpm.costoUnit_I.setModel(new SpinnerNumberModel(0, 0, 999999999, 1000));

        vpm.nivel_E.setModel(new SpinnerNumberModel(0, 0, 99, 1));
        vpm.orden_E.setModel(new SpinnerNumberModel(0, 0, 99, 1));
        vpm.costoUnit_E.setModel(new SpinnerNumberModel(0, 0, 999999999, 1000));

        codPAD = Integer.parseInt(vpm.padCodPartida_I.getSelectedItem().toString());
        if (codPAD == 0) {
            vpm.nivel_I.setValue(1);
            vpm.orden_I.setValue(new PPartida_MezclaDAO().asignarOrden("I", 1, varCodCiaGlobalDeLogin));
            vpm.nivel_I.setEnabled(false);
            vpm.orden_I.setEnabled(false);
        }
        codPAD = Integer.parseInt(vpm.padCodPartida_E.getSelectedItem().toString());
        if (codPAD == 0) {
            vpm.nivel_E.setValue(1);
            vpm.orden_E.setValue(new PPartida_MezclaDAO().asignarOrden("E", 1, varCodCiaGlobalDeLogin));
            vpm.nivel_E.setEnabled(false);
            vpm.orden_E.setEnabled(false);
        }
    }

    int codPAD;

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == vpm.padCodPartida_I && vpm.padCodPartida_I.getSelectedItem() != null) {
            codPAD = Integer.parseInt(vpm.padCodPartida_I.getSelectedItem().toString());
            if (codPAD == 0) {
                vpm.nivel_I.setValue(1);
                vpm.orden_I.setValue(new PPartida_MezclaDAO().asignarOrden("I", 1, varCodCiaGlobalDeLogin));
                vpm.nivel_I.setEnabled(false);
                vpm.orden_I.setEnabled(false);
            } else {
                vpm.nivel_I.setValue(new PPartida_MezclaDAO().asignarNivelNoNULL("I", codPAD, varCodCiaGlobalDeLogin));
                vpm.orden_I.setValue(new PPartida_MezclaDAO().asignarOrdenNoNULL("I", codPAD, varCodCiaGlobalDeLogin));
                vpm.nivel_I.setEnabled(false);
                vpm.orden_I.setEnabled(false);
            }
        }

        if (e.getSource() == vpm.padCodPartida_E && vpm.padCodPartida_E.getSelectedItem() != null) {
            codPAD = Integer.parseInt(vpm.padCodPartida_E.getSelectedItem().toString());
            if (codPAD == 0) {
                vpm.nivel_E.setValue(1);
                vpm.orden_E.setValue(new PPartida_MezclaDAO().asignarOrden("E", 1, varCodCiaGlobalDeLogin));
                vpm.nivel_E.setEnabled(false);
                vpm.orden_E.setEnabled(false);
            } else {
                vpm.nivel_E.setValue(new PPartida_MezclaDAO().asignarNivelNoNULL("E", codPAD, varCodCiaGlobalDeLogin));
                vpm.orden_E.setValue(new PPartida_MezclaDAO().asignarOrdenNoNULL("E", codPAD, varCodCiaGlobalDeLogin));
                vpm.nivel_E.setEnabled(false);
                vpm.orden_E.setEnabled(false);
            }
        }
    }
}