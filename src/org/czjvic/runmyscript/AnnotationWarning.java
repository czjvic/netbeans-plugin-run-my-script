/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.czjvic.runmyscript;

import org.openide.text.Annotation;

public class AnnotationWarning extends Annotation {

    private final String message;

    public AnnotationWarning(String error) {
        this.message = error;
    }

    public String getAnnotationType() {
        return "org-czjvic-runmyscript-warning";
    }

    public String getShortDescription() {
        return message;
    }
}
