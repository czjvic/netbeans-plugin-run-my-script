package org.czjvic.runmyscript;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

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
        String cmd = Utils.getCommand();
        Command command = new Command(cmd);
        command.run();
    }

//https://platform.netbeans.org/tutorials/nbm-options.html
//http://bits.netbeans.org/8.0/javadoc/org-openide-text/org/openide/text/doc-files/api.html
}
