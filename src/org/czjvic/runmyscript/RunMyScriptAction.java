package org.czjvic.runmyscript;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.IOProvider;
import org.openide.windows.TopComponent;
import org.openide.loaders.DataObject;
import org.openide.filesystems.FileUtil;
import org.openide.windows.InputOutput;
import org.openide.util.NbPreferences;
import org.openide.cookies.LineCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.text.Annotatable;
import org.openide.text.Annotation;
import org.openide.text.Line;
import org.openide.util.Exceptions;

@ActionID(
        category = "Debug",
        id = "org.czjvic.runmyscript.RunMyScriptAction"
)
@ActionRegistration(
        iconBase = "org/czjvic/runmyscript/Actions-system-run-icon.png",
        displayName = "Run My Script"
)
@ActionReferences({
    @ActionReference(path = "Menu/Source", position = 850, separatorBefore = 845),
    @ActionReference(path = "Shortcuts", name = "DS-F12")
})
@Messages("CTL_RunMyScriptAction=Run My Script")
public final class RunMyScriptAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {

        FileObject currentFile = TopComponent.getRegistry().getActivated().getLookup().lookup(DataObject.class).getPrimaryFile();
        String currentFilePath = FileUtil.toFile(currentFile).getAbsolutePath();

    
        //        String scriptPath = NbPreferences.forModule(RunMyScriptPanel.class).get("path", "");
        //        String cmd = scriptPath.replace("$CURRENT_FILE$", currentFilePath);
        //
        //        InputOutput outputWindow = IOProvider.getDefault().getIO("Run My Script", false);
        //        outputWindow.closeInputOutput();
        //        outputWindow = IOProvider.getDefault().getIO("Run My Script", true);
        //
        //        outputWindow.getOut().println("Starting command: " + cmd);
        //        outputWindow.select();
        //
        //        String enviromentVariable = NbPreferences.forModule(RunMyScriptPanel.class).get("enviroment", "");
        //
        //        String[] env = {enviromentVariable};
        //        try {
        //            Process process = Runtime.getRuntime().exec(cmd, env);
        //
        //            BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
        //            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        //
        //            String s = null;
        //            while ((s = stdOut.readLine()) != null) {
        //                outputWindow.getOut().println(s);
        //            }
        //
        //            while ((s = stdError.readLine()) != null) {
        //                outputWindow.getOut().println(s);
        //            }
        //
        //        } catch (IOException ex) {
        //            ex.printStackTrace();
        //            outputWindow.getOut().println("Failed running command. Exception message: " + ex.getMessage());
        //        }

    }

    private void addAnotation(int lineNumber, String message, boolean isError) throws DataObjectNotFoundException {
        FileObject currentFile = TopComponent.getRegistry().getActivated().getLookup().lookup(DataObject.class).getPrimaryFile();

        DataObject objWithError = DataObject.find(currentFile);

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
                    // User edited the line, assume error should be cleared.
                    ann.detach();
                    line.removePropertyChangeListener(this);
                }
            }
        });
    }

    
    
    //https://platform.netbeans.org/tutorials/nbm-options.html
//http://bits.netbeans.org/8.0/javadoc/org-openide-text/org/openide/text/doc-files/api.html
}
