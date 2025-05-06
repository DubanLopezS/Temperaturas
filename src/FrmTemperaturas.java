import javax.swing.*;
import com.toedter.calendar.JDateChooser;

import entidades.Temperatura;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;

import java.util.List;
import java.util.Map;


import servicios.ServicioTemperatura;


public class FrmTemperaturas extends JFrame {


    private JDateChooser dccDesde, dccHasta, dccEspecifica;
    private JTabbedPane tpTemperatura;
    private JPanel pnlGrafica;
    private JPanel pnlCiudades;
    private List<Temperatura> datos;

    public FrmTemperaturas() {
        setTitle("Registros de Temperatura");
        setSize(700, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JToolBar tb = new JToolBar();

        JButton btnCiudades = new JButton();
        btnCiudades.setIcon(new ImageIcon(getClass().getResource("/iconos/Datos.png")));
        btnCiudades.setToolTipText("Ciudades de la fecha seleccionada");
        btnCiudades.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnCiudadesClick();
            }
        });
        tb.add(Box.createHorizontalStrut(80));
        tb.add(btnCiudades);

        
        JButton btnGraficar = new JButton();
        btnGraficar.setIcon(new ImageIcon(getClass().getResource("/iconos/Grafica.png")));
        btnGraficar.setToolTipText("Grafica Temperaturas vs Ciudad");
        btnGraficar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                //btnGraficarClick();
            }
        });
        tb.add(Box.createHorizontalStrut(300));
        tb.add(btnGraficar);


        JPanel pnlTemperatura = new JPanel();
        pnlTemperatura.setLayout(new BoxLayout(pnlTemperatura, BoxLayout.Y_AXIS));

        JPanel pnlDatosProceso = new JPanel();
        pnlDatosProceso.setPreferredSize(new Dimension(0, 50));
        pnlDatosProceso.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        pnlDatosProceso.setLayout(null);

        JLabel lblEspecifica = new JLabel("Fecha Especifica");
        lblEspecifica.setBounds(10, 10, 100, 25);
        pnlDatosProceso.add(lblEspecifica);

        JLabel lblDesde = new JLabel("Desde");
        lblDesde.setBounds(300, 10, 100, 25);
        pnlDatosProceso.add(lblDesde);

        JLabel lblHasta = new JLabel("Hasta");
        lblHasta.setBounds(450, 10, 100, 25);
        pnlDatosProceso.add(lblHasta);

        dccEspecifica = new JDateChooser();
        dccEspecifica.setBounds(120, 10, 100, 25);
        pnlDatosProceso.add(dccEspecifica);

        dccDesde = new JDateChooser();
        dccDesde.setBounds(340, 10, 100, 25);
        pnlDatosProceso.add(dccDesde);

        dccHasta = new JDateChooser();
        dccHasta.setBounds(490, 10, 100, 25);
        pnlDatosProceso.add(dccHasta);

        pnlGrafica = new JPanel();
        JScrollPane spGrafica = new JScrollPane(pnlGrafica);

        pnlCiudades = new JPanel();
        pnlCiudades.setBackground(new Color(230, 230, 250)); // Ponerle algo de color al panel

        tpTemperatura = new JTabbedPane();
        tpTemperatura.addTab("Gráfica", spGrafica);
        tpTemperatura.addTab("Temp Menor y Mayor", pnlCiudades);

        //Agregar componentes
        pnlTemperatura.add(pnlDatosProceso);
        pnlTemperatura.add(tpTemperatura);

        getContentPane().add(tb, BorderLayout.NORTH);
        getContentPane().add(pnlTemperatura, BorderLayout.CENTER);

        cargarDatos();
    }

    private void cargarDatos() {
        datos = ServicioTemperatura.getDatos(System.getProperty("user.dir") + "/src/datos/Temperaturas.csv");
    }
    

    //private void btnGraficarClick() {
        

    //}
    

    private void btnCiudadesClick() {

        LocalDate especifica = dccEspecifica.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // Cambiar a la pestaña de temperatura menor y mayor
        tpTemperatura.setSelectedIndex(1);

        var ciudades = ServicioTemperatura.getCiudades(datos, especifica);

        pnlCiudades.removeAll();
        pnlCiudades.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        int fila = 0;
        for (Map.Entry<String, String> ciudad : ciudades.entrySet()) {
            gbc.gridx = 0;
            gbc.gridy = fila;
            pnlCiudades.add(new JLabel(ciudad.getKey()), gbc); // Ciudad

            gbc.gridx = 1;
            pnlCiudades.add(new JLabel(ciudad.getValue()), gbc); // Temperatura

            gbc.insets = new Insets(5, 10, 5, 10); // Espacio para que se vea mas organizado
            
            fila++;

        pnlCiudades.revalidate();
        pnlCiudades.repaint();
        
        }

    }

}
 



