import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MantenimientoFichero implements Serializable {

    /*
    Este fichero tratará de una base de datos que guardara Hospitales.
    Se escribirá de esta forma:
        codigo(Int-4bytes)nombreHospital(String-40bytes)especialidad(int-4bytes)numEmpleados(Int-4bytes)
        
    Por lo que hay un total de: 52bytes por cada hospital
    
    La especialidad se guardará por numeros (ya que es un codigo interno para identificar por ejemplo Psiquiatria, Reumatologia etc..)
     */
    Scanner reader = new Scanner(System.in);
    //Se utilizara un fichero guardado en la carpeta raiz del proyecto.
    File fichero = new File(".\\temp.dat");

    public void firstOption() {
        //System.out.println("Codigo es: " + getCodigo(this.fichero));

        System.out.println("Vas a añadir un nuevo hospital, por lo que tienes que introducir todos sus datos: ");
        System.out.println("Introduce el nombre del hospital: ");
        String name = reader.next();
        System.out.println("Introduce su especialidad(0-9): ");
        int especiality = reader.nextInt();
        System.out.println("Introduce su número de empleados");
        int numEmpleados = reader.nextInt();
        if (añadirEmpleado(name, especiality, numEmpleados) == 1) {
            System.out.println("Se ha añadido correctamente");
            menu();
        } else {
            System.out.println("No se ha añadido correctamente.");
        }
    }

    public int añadirEmpleado(String name, int especiality, int numEmpleados) {
        long pos;

        if (getCodigo(this.fichero) == -1) {
            System.out.println("Ha habido un problema por favor, vuelva a intentarlo...");
            return -1;
        } else {
            try {
                RandomAccessFile raf = new RandomAccessFile(this.fichero, "rw");
                pos = raf.length();
                raf.seek(pos);
                raf.writeInt(getCodigo(this.fichero));//codigo
                StringBuffer buffer = new StringBuffer(name);
                buffer.setLength(20);
                raf.writeChars(buffer.toString());//Nombre hospital
                raf.writeInt(especiality);//especialidad
                raf.writeInt(numEmpleados);//numero de empleados
                raf.close();
                return 1;
            } catch (FileNotFoundException e) {
                System.out.println("El fichero dado no se ha encontrado, por favor vuelva a intentarlo.");
            } catch (IOException ex) {
                System.out.println("Ha habido un error de escritura en el archivo");
            }
        }
        return 0;
    }

    public int getCodigo(File file) {
        //Esta funcion es para saber cual es el ultimo codigo que podemos poner
        int codigo = -1;
        try {
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            codigo = (int) (raf.length() / 52) + 1;
            return codigo;
        } catch (FileNotFoundException e) {
            System.out.println("Ha habido un problema con el fichero, por favor vuelva a intentarlo");
        } catch (IOException ex) {
            System.out.println("ERROR!\nSALIENDO...");
            System.exit(0);
        } finally {
            return codigo;
        }
    }

    public void secondOption() {
        //Segun el codigo que nos den, sabemos en que posicion estará este hospital
        int pos;

        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(this.fichero, "rw");
        } catch (FileNotFoundException ex) {
            System.out.println("No se encuentra el fichero");
        }

        System.out.println("Introduce el id del hospital a visualizar: ");
        pos = reader.nextInt();
        int puntero = (pos - 1) * 52;

        //Compruebo que ese hospital exista
        if (puntero >= fichero.length()) {
            System.out.println("Este hospital no existe");
            menu();
        } else {
            try {
                //Visualizo el hospital que le pase por la variable pos
                visualizarEmpleado(raf, pos);
                menu();
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("Error en la lectura del fichero");
            }
        }
        try {
            raf.close();
        } catch (IOException ex) {
            System.out.println("Error al cerrar el fichero");
        }
    }

    public void visualizarEmpleado(RandomAccessFile raf, int pos) throws IOException {
        char nombreV[] = new char[20];
        int id, especiality, numEmpleados;
        char aux;
        try {
            //Lectura de los datos de un usuario dado
            raf.seek((pos - 1) * 52);

            id = raf.readInt();
            for (int i = 0; i < nombreV.length; i++) {
                aux = raf.readChar();
                nombreV[i] = aux;
            }
            String nombreN = new String(nombreV);
            especiality = raf.readInt();
            numEmpleados = raf.readInt();

            if (id > 0) {
                System.out.printf("ID: %s, Nombre: %s, Especialidad: %s, Num Empleados: %s\n", id, nombreN.trim(), especiality, numEmpleados);
                System.out.println("");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Error en la lectura del fichero");
        }
    }

    public void thirdOption() {
        char nombreV[] = new char[20];
        int id, especiality, numEmpleados;
        char aux;
        System.out.println("Introduce la Especialidad que quieres visualizar(0-9)");
        int especialityo = reader.nextInt();
        RandomAccessFile raf;
        try {
            raf = new RandomAccessFile(this.fichero, "rw");
            int userCount = (int) (raf.length() / 52) + 1;

            for (int i = 1; i < userCount; i++) {
                raf.seek((i - 1) * 52);

                id = raf.readInt();
                for (int j = 0; j < nombreV.length; j++) {
                    aux = raf.readChar();
                    nombreV[j] = aux;
                }
                String nombreN = new String(nombreV);
                especiality = raf.readInt();
                numEmpleados = raf.readInt();
                if (especiality == especialityo) {

                    if (id > 0) {
                        System.out.printf("ID: %s, Nombre: %s, Especialidad: %s, Num Empleados: %s\n", id, nombreN.trim(), especiality, numEmpleados);
                        System.out.println("");
                    }
                }
            }
            raf.close();
            System.out.println("\nVolviendo al menu...\n");
            menu();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MantenimientoFichero.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MantenimientoFichero.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void fourthOption() {
        System.out.println("Introduce el ID del hospital que quieras modificar: ");
        int hospital = reader.nextInt();
        int puntero = (hospital - 1) * 52;
        //Compruebo que ese hospital exista
        if (puntero >= fichero.length()) {
            System.out.println("Este hospital no existe");
            menu();

        } else {
            System.out.println("Elige que dato quieres modificar:\n1 - Nombre Hospital\n2 - Especialidad\n3 - Numero de empleados");
            int option = reader.nextInt();

            RandomAccessFile raf;
            try {
                raf = new RandomAccessFile(this.fichero, "rw");
                if (option == 1) {
                    //Colocamos el puntero en el nombre
                    raf.seek((hospital - 1) * 52 + 4);
                    System.out.println("Escribe el nombre a cambiar: ");
                    String name = reader.next();
                    StringBuffer buffer = new StringBuffer(name);
                    buffer.setLength(20);
                    raf.writeChars(buffer.toString());
                    System.out.println("Se ha cambiado correctamente!");
                } else if (option == 2) {
                    //Colocamos el puntero en el departamento
                    raf.seek((hospital - 1) * 52 + 4 + 40);
                    System.out.println("Escribe la nueva especialidad");
                    int dep = reader.nextInt();
                    raf.writeInt(dep);
                    System.out.println("Se ha cambiado correctamente!");
                } else if (option == 3) {
                    //Colocamos el puntero en el salario
                    raf.seek((hospital - 1) * 52 + 4 + 40 + 4);
                    System.out.println("Escribe el nuevo número de Empleados");
                    int sal = reader.nextInt();
                    raf.writeInt(sal);
                    System.out.println("Se ha cambiado correctamente!");

                } else {
                    System.out.println("Esa opcion no esta contemplada..\n Volviendo al menu\n");
                }
                raf.close();
                menu();

            } catch (FileNotFoundException ex) {
                Logger.getLogger(MantenimientoFichero.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MantenimientoFichero.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void fifthOption() throws FileNotFoundException, IOException{
        System.out.println("Introduce el id del hospital a eliminar");
        int id = reader.nextInt();
        int puntero = (id - 1) * 52;

        RandomAccessFile raf = new RandomAccessFile(this.fichero, "rw");;
    
        
        if (puntero >= fichero.length()) {
            System.out.println("Este hospital no existe");
            menu();
            
        } else {
            visualizarEmpleado(raf, id);
            System.out.println("Desea eliminarlo(y/n):");
            String question = reader.next();
            if (question.equals("y")){
                try {
                    
                    raf.seek((id - 1)*52);
                    raf.writeInt(-1);
                    raf.close();
                    System.out.println("Hospital eliminado!\nVolviendo...\n");
                    menu();

                } catch (FileNotFoundException ex) {
                    Logger.getLogger(MantenimientoFichero.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(MantenimientoFichero.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else{
                System.out.println("\nRegresando al menu...\n");
            }
        }
        
    }
    
    public void menu() {
        //menu para comprobar la opcion que esta eligiendo.
        int opcion;
        System.out.println("Introduce una opcion:\n" +
                            "1: Añadir un hospital.\n" +
                        "2: Visualizar los datos de un hospital.\n" +
                        "3: Visualizar todos los hospitales de una especialidad\n" +
                        "4: Modificar un dato de un hospital\n" +
                        "5: Eliminar un hospital\n" +
                        "6: Salir");
        try {
            opcion = reader.nextInt();
            switch (opcion) {
                case 1:
                    System.out.println("Has entrado en la opcion 1");
                    this.firstOption();
                    break;

                case 2:
                    System.out.println("Has entrado en la opcion 2");
                    this.secondOption();
                    break;

                case 3:
                    System.out.println("Has entrado en la opcion 3");
                    this.thirdOption();
                    break;

                case 4:
                    System.out.println("Has entrado en la opcion 4");
                    this.fourthOption();
                    break;

                case 5:
                    System.out.println("Has entrado en la opcion 5");
                    this.fifthOption();
                    break;
                case 6:
                    System.out.println("Saliendo...");
                    System.exit(0);
                default:
                    System.out.println("Esa sentencia no esta contemplada. Por favor vuelva a elegir una opcion");
                    menu();
            }
        } catch (java.util.InputMismatchException e) {
            System.out.println("Opcion no contemplada...\nSaliendo...");
            System.exit(0);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MantenimientoFichero.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MantenimientoFichero.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) throws IOException {
        MantenimientoFichero edf = new MantenimientoFichero();
        edf.menu();
    }
}