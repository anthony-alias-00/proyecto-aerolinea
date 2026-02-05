package vuelosfis.controlador;

import vuelosfis.vista.VentanaPrincipal;

public class MainVuelosFIS {

    public static void main(String[] args) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            System.err.println("Error visual.");
        }

        java.awt.EventQueue.invokeLater(() -> {
            VentanaPrincipal vista = new VentanaPrincipal();
            ControladorVuelos controlador = new ControladorVuelos(vista);
            controlador.iniciar();
            System.out.println("Sistema VuelosFIS listo. ✈️");
        });
    }
}