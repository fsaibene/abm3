package com.herprogramacion.crunch_expenses.utils;

/**
 * Constantes
 */
public class Constantes {

    /**
     * Puerto que utilizas para la conexión.
     * Dejalo en blanco si no has configurado esta característica.
     */
    private static final String PUERTO_HOST = ":80";

    /**
     * Dirección IP de genymotion o AVD
     */
    private static final String IP = "http://192.168.0.15";

    /**
     * URLs del Web Service
     */
    public static final String GET_URL = IP + PUERTO_HOST + "/Crunch_Expenses/web/obtener_gastos.php";
    public static final String INSERT_URL = IP + PUERTO_HOST + "/Crunch_Expenses/web/insertar_gasto.php";
    public static final String DELETE_URL = IP + PUERTO_HOST + "/Crunch_Expenses/web/borrar_gasto.php";
    public static final String UPDATE_URL = IP + PUERTO_HOST + "/Crunch_Expenses/web/actualizar_registro.php";

    /**
     * Campos de las respuestas Json
     */
    public static final String ID_GASTO = "idGasto";
    public static final String ESTADO = "estado";
    public static final String GASTOS = "gastos";
    public static final String MENSAJE = "mensaje";

    /**
     * Códigos del campo {@link ESTADO}
     */
    public static final String SUCCESS = "1";
    public static final String FAILED = "2";

    /**
     * Tipo de cuenta para la sincronización
     */
    public static final String ACCOUNT_TYPE = "com.herprogramacion.crunch_expenses.account";


}
