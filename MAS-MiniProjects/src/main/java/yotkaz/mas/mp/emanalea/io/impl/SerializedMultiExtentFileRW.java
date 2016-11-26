package yotkaz.mas.mp.emanalea.io.impl;

import com.google.common.collect.ListMultimap;
import com.google.common.io.Files;
import yotkaz.mas.mp.emanalea.io.RWCombo;

import java.io.*;

/**
 * Created on 11.04.16.
 */
public class SerializedMultiExtentFileRW implements RWCombo<ListMultimap<Class, Object>> {

    private File file;
    private File fileTmp;

    public SerializedMultiExtentFileRW(String path) throws IOException {
        file = new File(path);
        fileTmp = new File(path + ".tmp");
    }

    @Override
    public ListMultimap<Class, Object> read() throws Exception {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        ListMultimap<Class, Object> result = (ListMultimap<Class, Object>) ois.readObject();
        ois.close();
        return result;
    }

    @Override
    public void write(ListMultimap<Class, Object> extensions) throws Exception {
        ObjectOutputStream oosTmp = new ObjectOutputStream(new FileOutputStream(fileTmp));
        oosTmp.writeObject(extensions);
        oosTmp.close();
        Files.move(fileTmp, file);
        fileTmp.delete();
    }
}
