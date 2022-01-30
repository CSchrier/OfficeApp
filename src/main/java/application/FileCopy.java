package application;

import java.io.*;


public class FileCopy {

    FileCopy(File from, File to) throws IOException {
        System.out.println("from "+from);
        System.out.println("to "+to);
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(from));
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(to));




        byte[] buffer = new byte[1024];
        int lengthRead;
        while ((lengthRead = in.read(buffer)) > 0) {
            out.write(buffer, 0, lengthRead);
            out.flush();
        }
        in.close();
        out.close();
    }
}