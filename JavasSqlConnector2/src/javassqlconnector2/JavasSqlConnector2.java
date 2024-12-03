package javassqlconnector2;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jose Alexander Sotelo Garcia
 */
public class JavasSqlConnector2 {

    public static void main(String[] args) {
        //Declaro variable para admitir la entrada de datos
        Scanner eT = new Scanner(System.in);
        
        //Declaro variable para almazenar la opcion seleccionada por el usuario
        int opt;
        
        //Declaro variables donde almazeno los datos ingresados por el usuario
        String n,c,p,pc;
        
        //Declaro variables para poder conectarme a la base de datos
        String usuario = "root";
        String clave = "";
        String url = "jdbc:mysql://localhost:3306/programacionii";
        
        //Declaro dependencias para conectarme
        Connection con;
        Statement stmt;
        ResultSet rs;
        
        //Me conecto con la libreria
        try {
            //Realizamos la conexion con la libreria
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(JavasSqlConnector2.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Le doy instrucciones al usuario
        System.out.println("Seleccione una opcion:\n[1] - Añadir usuario a la base de datos\n[2] - Actualizar informacion de X usuario\n[3] - Eliminar a un usuario de la base de datos\n[4] - Mostrar usuarios de la base de datos");
        //Leo la entrada del usuario
        opt = eT.nextInt();
        
        //Con un switch selecciono la opcion ingresada por el usuario
        switch(opt){
            case 1->{//Si el usuario selecciono añadir usuario a la base de datos
                try {//Realizo la conexion con la base de datos
                    con = DriverManager.getConnection(url,usuario,clave);
                    //Inisializo el statement
                    stmt = con.createStatement();
                    //Le pido al usuario que ingrese la informacion
                    System.out.println("Ingrese su nombre: ");
                    n = eT.next();
                    System.out.println("Ingrese su correo: ");
                    c = eT.next();
                    //Ejecuto query para verificar si existe algun usuario con el correo asignado
                    rs = stmt.executeQuery("SELECT correo FROM usuario WHERE correo = '" + c + "'");
                    if(rs.next()){//Si el correo ya esta registrado se lo damos a saber al usuario
                        System.out.println("ERORR: YA EXISTE UN USUARIO CON ESE CORREO ASIGNADO");
                    }else{//Si el correo no esta registrado seguimos con el programa
                        //Le pedimos la contraseña
                        System.out.println("Ingrese su contraseña: ");
                        p = eT.next();
                        //Le pedimos que confirme la contraseña
                        System.out.println("Confirme su contrasema:");
                        pc = eT.next();
                        //Comparamos las contraseñas dadas
                        if(p.equals(pc)){//Si las contraseñas son iguales seguimos con el programa
                            //Insertamos la sentencia sql
                            stmt.executeUpdate("INSERT INTO usuario VALUES(null,'" + n + "','" + c + "','" + p + "')");
                            //Le damos un mensaje al usuario diciendole que se creeo el usuario con exito
                            System.out.println("Usuario creado con EXITO");
                        }else{//Si la contraseña es diferente se lo damos a saber al usuario
                            System.out.println("ERROR: LAS CONTRASEÑAS NO COINCIDEN");
                        }
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(JavasSqlConnector2.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            case 2->{//Si el usuario selecciono la opcion de actualizar informacion de un usuario
                try{//Realizo la conexion con la base de datos
                    con = DriverManager.getConnection(url,usuario,clave);
                    //Inicializo el statement
                    stmt = con.createStatement();
                    //Inicializo el resultSet
                    rs = stmt.executeQuery("SELECT * FROM usuario");
                    //Salto al siguiente elemento de la tabla
                    rs.next();
                    //Le muestro al usuario los usaurios dentro de la base de datos
                    do{
                        System.out.println("- Nombre: " + rs.getString("nombre") + " Correo: " + rs.getString("correo") + " Contraseña: " + rs.getString("password"));
                    }while(rs.next());
                    //Le doy instrucciones al usuario
                    System.out.println("Seleccione el correo electronico del usuario a modificar informacion:");
                    //Leeo la entrada del usuario
                    c = eT.next();
                    //Ejecuto query para verificar si existe usuario con el correo dado
                    rs = stmt.executeQuery("SELECT correo FROM usuario WHERE correo = '" + c + "'");
                    if(rs.next()){
                        //Le pregunto al usuario que desea modificar de dicho usuario seleccionado
                        System.out.println("Seleccione la opcion que desea actualizar:\n[1] - Nombre\n[2] - Contraseña");
                        //Leeo la entrada del usuario
                        opt = eT.nextInt();
                        switch(opt){
                            case 1->{//Si el usuario selecciono la opcion de actualizar nombre 
                                //Le pido la informacion al usuario
                                System.out.println("Ingrese el nuevo nombre a asignar a " + c);
                                //Leeo la entrada del usuario
                                n = eT.next();
                                //Ejecuto la sentencia sql
                                stmt.executeUpdate("UPDATE usuario SET nombre = '" + n + "' WHERE correo = '" + c + "'");
                                System.out.println("Nombre ACTUALIZADO con EXITO");
                            }
                            case 2->{//Si el usuario selecciono la opcion de actualizar contraseña
                                //Le pido la informacion al usuario
                                System.out.println("Ingrese su contraseña:");
                                //Leeo la entrada del usuario
                                p = eT.next();
                                //Inicializo resulSet
                                rs = stmt.executeQuery("SELECT * FROM usuario WHERE correo = '" + c + "'");
                                if(rs.next()){
                                    //Compruebo si la contraseña ingresada es la CORRECTA
                                    if(p.equals(rs.getString("password"))){
                                        System.out.println("DIJITE su nueva contraseña:");
                                        //Leeo la entrada del usuario
                                        p = eT.next();
                                        //Le pido al usuario que confirme su contraseña
                                        System.out.println("Confirme su contraseña:");
                                        //Leeo la entrada del usuario
                                        pc = eT.next();
                                        //Compruebo que las contraseñas sean correctas
                                        if(p.equals(pc)){//Si las contraseñas son iguales
                                            //Ejecuto la query para actualizar la contraseña
                                            stmt.executeUpdate("UPDATE usuario SET password = '" + p + "' WHERE correo = '" + c + "'");
                                            //Le muestro un mensaje al usuario
                                            System.out.println("Contraseña ACTUALIZADA con EXITO");
                                        }else{//Si las contraseñas no son iguales
                                            System.out.println("ERROR: LAS CONTRASEÑAS NO COINCIDEN");
                                        }
                                    }else{
                                        System.out.println("ERROR: CONTRASEÑA NO VALIDA!");
                                    }
                                }else{
                                    System.out.println("OCURRIO UN ERROR EN LA PETICION AL SERVIDOR...");
                                }
                            }
                            default->{
                                System.out.println("ERROR: DIJITO NO VALIDO");
                            }
                        }
                    }else{
                        System.out.println("No existe ese correo...");
                    }
                }catch(SQLException ex){
                    Logger.getLogger(JavasSqlConnector2.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            case 3->{//Si el usuario selecciono eliminar a algun usuario de la bas de datos
                try {//Realizo la conexion con la base de datos
                    con = DriverManager.getConnection(url,usuario,clave);
                    //Inisializo el statement
                    stmt = con.createStatement();
                    //Inicializo el resultSet
                    rs = stmt.executeQuery("SELECT * FROM usuario");
                    //Salto al siguiente elemento de la tabla
                    rs.next();
                    //Le muestro al usuario los usuarios dentro de la base de datos
                    do{
                        System.out.println("- Nombre: " + rs.getString("nombre") + " Correo: " + rs.getString("correo") + " Contraseña: " + rs.getString("password"));
                    }while(rs.next());
                    //Le doy instrucciones al usuario
                    System.out.println("Ingrese el correo electronico de la persona a eliminar de la base de datos:");
                    c = eT.next(); //Leo la entrada del usuario
                    //Ejecuto query para verificar si existe ese correo
                    rs = stmt.executeQuery("SELECT correo FROM usuario WHERE correo = '" + c + "'");
                    //Compruebo si el correo dado existe en la tabla de datos
                    if(rs.next()){//SI el correo fue encontrado
                        //Le pido al usuario que ingrese la contraseña de la cuenta
                        System.out.println("Ingrese la contraseña de la cuenta de " + c);
                        //Leeo la entrada del usuario
                        p = eT.next();
                        //Ejecuto una query para verificar si la contrasña es valida
                        rs = stmt.executeQuery("SELECT password FROM usuario WHERE password = '" + p + "'");
                        //Compruebo si la contraseña es valida
                        if(rs.next()){//Si la contraseña es valida
                            //Ejecuto la sentencia para eliminar la fila de la base de datos
                            stmt.executeUpdate("DELETE FROM usuario WHERE correo = '" + c + "'");
                            //Le muestro un mensaje al usuario
                            System.out.println("Usuario ELIMINADO con EXITO");
                        }else{//Le muestro al usuario un error al ser contraseña incorrecta
                            System.out.println("ERROR: CONTRASEÑA INCORRECTA");
                        }
                    }else{//Si el correo no fue encontrado
                        System.out.println("ERROR: CORREO NO VALIDO!");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(JavasSqlConnector2.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            case 4->{//Si el usuario selecciono mostrar a los usuarios
                //Realizo la conexion con la base de datos
                try{//Realizamos la conexion con la base de datos
                    con = DriverManager.getConnection(url,usuario,clave);
                    //Inisializo el statement
                    stmt = con.createStatement();
                    //Inisializo el resultSet
                    rs = stmt.executeQuery("SELECT * FROM usuario");
                    //Salto al siguiente elemento de la tabla
                    rs.next();
                    //Le muestro los registros al usuario
                    do{
                        System.out.println("- Nombre: " + rs.getString("nombre") + " Correo: " + rs.getString("correo") + " Contraseña: " + rs.getString("password"));
                    }while(rs.next());
                }catch(SQLException ex){
                    Logger.getLogger(JavasSqlConnector2.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            default ->{//SI el usuario ingreso un dijito no valido
                System.out.println("ERROR: DIJITO NO VALIDO!");
            }
        }
    }
}