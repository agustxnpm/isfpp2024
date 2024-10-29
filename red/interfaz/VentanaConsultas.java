package red.interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.List;

import red.modelo.Equipo;
import red.modelo.Conexion;
import red.negocio.Calculo;
import red.negocio.Red;
import red.servicio.EquipoService;
import red.servicio.ConexionService;
import red.servicio.EquipoServiceImp;
import red.servicio.TipoCableServiceImp;
import red.servicio.ConexionServiceImp;

public class VentanaConsultas extends JFrame {
 
    private Calculo calculo;
    private Red red;
    
    private JButton calcularVelocidadButton;
    private JButton pingEquipoButton;
    private JButton detectarProblemasButton;
    private JButton calcularButton; // boton para confirmar el calculo de la velocidad


    public VentanaConsultas(Calculo calculo, Red red) {
        setTitle("Consultas de la Red");
        setSize(600, 400);
        setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        this.calculo = calculo;
		this.red = red;


		Handler handler = new Handler();

     
        calcularVelocidadButton = new JButton("Calcular Velocidad Máxima");
        calcularVelocidadButton.setBounds(23, 120, 171, 69);
        calcularVelocidadButton.addActionListener(handler);
  

        pingEquipoButton = new JButton("Realizar Ping a Equipo");
        pingEquipoButton.setBounds(206, 120, 171, 69);
        pingEquipoButton.addActionListener(handler);
 

        detectarProblemasButton = new JButton("Detectar Problemas");
        detectarProblemasButton.setBounds(387, 120, 171, 69);
        detectarProblemasButton.addActionListener(handler);
 

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.add(calcularVelocidadButton);
        panel.add(pingEquipoButton);
        panel.add(detectarProblemasButton);

        getContentPane().add(panel, BorderLayout.CENTER);
    }

    private void ventanaVelocidad() {
    	
    	calcularButton = new JButton("Calcular");
        
        JPanel panelCentral = new JPanel();
        panelCentral.add(new JLabel("Seleccione los equipos para calcular velocidad"));

        /**** componentes para elegir los equipos y calcular su velocidad ****/
        
        
        JDialog dialog = new JDialog(this, "Calcular Velocidad", true);
        
        JPanel panelInferior = new JPanel();
       
        panelInferior.add(calcularButton);
        
        dialog.add(panelCentral, BorderLayout.CENTER);
        dialog.add(panelInferior, BorderLayout.SOUTH);
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

    	
    }

    // Method to perform ping on a selected team
    private void realizarPingAEquipo() {
        String direccionIp = JOptionPane.showInputDialog(this, "Ingrese la dirección IP del equipo:");
        boolean respuestaPing = calculo.realizarPingAEquipo(direccionIp);

        if (respuestaPing) {
            JOptionPane.showMessageDialog(this, "Ping exitoso al equipo con IP: " + direccionIp);
        } else {
            JOptionPane.showMessageDialog(this, "Ping fallido o equipo no encontrado.");
        }
    }

    private void detectarProblemasConectividad() {
        try {
            Equipo equipo = red.buscarEquipoPorCodigo("codigo"); // Example code
            Equipo gateway = red.buscarEquipoPorCodigo("codigo"); // Example code

            calculo.verificarConectividad(equipo, gateway); // Perform connectivity check
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al buscar el equipo o Gateway.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    

	private class Handler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if (e.getSource().equals(calcularVelocidadButton))
				ventanaVelocidad();
			
			if (e.getSource().equals(pingEquipoButton))
                realizarPingAEquipo();
			
			if (e.getSource().equals(detectarProblemasButton))
                detectarProblemasConectividad();
		}
		
	}
}

	
