
//récupéré chez Mr.Beuvron
package fr.insa.theo.encheresge2t2.gui;
import java.io.*;

public class ConsoleFdB implements java.io.Serializable {

    static final long serialVersionUID = 30101L;

    
    public static void println(String mess) {
        System.out.println(mess);
    }

        public static void print(String mess) {
        System.out.print(mess);
    }

    
    public static int entreeEntier(String s) {
        boolean encore = true;
        int res = 0;

        while (encore) {
            try {
                println(s);
                res = Integer.parseInt(myinput.readLine());
                encore = false;
            } catch (IOException e) {
                throw new Error(e);
            } catch (NumberFormatException e) {
                println("entier non valide, essayez encore");
                encore = true;
            }
        }
        return res;
    }

   
    public static int entreeInt(String s) {
        return entreeEntier(s);
    }

    
    public static long entreeLong(String s) {
        boolean encore = true;
        long res = 0;

        while (encore) {
            try {
                println(s);
                res = Long.parseLong(myinput.readLine());
                encore = false;
            } catch (IOException e) {
                throw new Error(e);
            } catch (NumberFormatException e) {
                println("entier non valide, essayez encore");
                encore = true;
            }
        }
        return res;
    }

   
    public static boolean entreeBooleanON(String s) {
        String rep = "";

        while ((rep.compareToIgnoreCase("O") != 0) && (rep.compareToIgnoreCase("N") != 0)) {
            try {
                println(s);
                rep = myinput.readLine();
            } catch (IOException e) {
                throw new Error(e);
            }
        }
        return rep.compareToIgnoreCase("O") == 0;
    }

    
    public static double entreeDouble(String s) {
        boolean encore = true;
        double res = 0;

        while (encore) {
            try {
                println(s);
                res = Double.parseDouble(myinput.readLine());
                encore = false;
            } catch (IOException e) {
                throw new Error(e);
            } catch (NumberFormatException e) {
                println("flottant non valide, essayez encore");
                encore = true;
            }
        }
        return res;
    }

   
    public static char entreeChar(String s) {
        boolean encore = true;
        char res = ' ';

        while (encore) {
            try {
                println(s);
                String rep = myinput.readLine();
                if (rep != null && rep.length() > 0) {
                    res = rep.charAt(0);
                    encore = false;
                }
            } catch (IOException e) {
                throw new Error(e);
            }
        }
        return res;
    }

    public static String entreeString(String s) {
        String res = null;

        try {
            println(s);
            res = myinput.readLine();
        } catch (IOException e) {
            throw new Error(e);
        }
        return res;
    }

    
    public static String entreeTexte(String s) {
        boolean encore = true;
        String res = null;

        try {
            println(s);
            println("(tapez une ligne contenant seulement \"fin\" pour arreter la saisie)");
            res = "";
            String cur = "";
            while (cur.compareTo("fin") != 0) {
                cur = myinput.readLine();
                if (cur.compareTo("fin") != 0) {
                    res = res + cur;
                }
            }
        } catch (IOException e) {
            System.exit(1);
        }
        return res;
    }
   
    private static BufferedReader myinput = new BufferedReader(new InputStreamReader(System.in));
    }
