/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.czjvic.runmyscript;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.openide.cookies.LineCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.text.Annotatable;
import org.openide.text.Annotation;
import org.openide.text.Line;
import org.openide.util.NbPreferences;
import org.openide.windows.TopComponent;

/**
 *
 * @author josefvrba
 */
public class Utils {

    /**
     * Add annotation to the line.
     *
     * @param lineNumber
     * @param message
     * @param isError
     *
     * @throws DataObjectNotFoundException
     */
    public static void addAnotation(int lineNumber, String message, boolean isError) {
        FileObject currentFile = getCurrentFile();

        DataObject objWithError = null;
        try {
            objWithError = DataObject.find(currentFile);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        LineCookie cookie = (LineCookie) objWithError.getLookup().lookup(LineCookie.class);

        Line.Set lineSet = cookie.getLineSet();
        final Line line = lineSet.getOriginal(lineNumber);

        final Annotation ann;
        if (isError) {
            ann = new AnnotationError(message);
        } else {
            ann = new AnnotationWarning(message);
        }

        ann.attach(line);
        line.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent ev) {
                String type = ev.getPropertyName();
                System.out.println(type);
                if (type == null || type == Annotatable.PROP_TEXT) {
                    ann.detach();
                    line.removePropertyChangeListener(this);
                }
            }
        });
    }

    /**
     * Return currently opened file.
     *
     * @return FileObject
     */
    public static FileObject getCurrentFile() {
        return TopComponent.getRegistry().getActivated().getLookup().lookup(DataObject.class).getPrimaryFile();
    }

    /**
     * Return compiled command with all values and parameters.
     *
     * @return String
     */
    public static String getCommand() {
        FileObject currentFile = getCurrentFile();
        String currentFilePath = FileUtil.toFile(currentFile).getAbsolutePath();
        String scriptPath = NbPreferences.forModule(RunMyScriptPanel.class).get("path", "");
        return scriptPath.replace("$CURRENT_FILE$", currentFilePath);
    }
}
