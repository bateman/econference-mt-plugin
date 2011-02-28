/**
 * This file is part of the eConference project and it is distributed under the 
 * terms of the MIT Open Source license.
 * 
 * The MIT License
 * Copyright (c) 2005 Collaborative Development Group - Dipartimento di Informatica, 
 *                    University of Bari, http://cdg.di.uniba.it
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this 
 * software and associated documentation files (the "Software"), to deal in the Software 
 * without restriction, including without limitation the rights to use, copy, modify, 
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to 
 * permit persons to whom the Software is furnished to do so, subject to the following 
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies 
 * or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A 
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT 
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF 
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE 
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package it.uniba.di.cdg.xcore.aspects;

import it.uniba.di.cdg.xcore.util.RunnableWithReturn;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

/**
 * Apply the worker object creation pattern upon SWT-related methods.
 */
public aspect SwtThreadSafety {
    /**
     * Captures synchronous methods: typically those that do not return anything.
     */
    protected pointcut uiSyncCalls() : execution( @SwtSyncExec * *.*(..) );

    /**
     * Captures asynchronous methods: methods that just update the UI and do not return 
     * any values are good candidates.
     */
    protected pointcut uiAsyncCalls() : execution( @SwtAsyncExec * *.*(..) );

    Object around() : uiSyncCalls() {
        RunnableWithReturn r = new RunnableWithReturn() {
            public void run() {
                try {
                    _returnValue = proceed();
                } catch (Throwable e) {
                    e.printStackTrace();
                    throw new RuntimeException( e ); // Soft the exception ...
                }
            }
        };
        
        getDisplay().syncExec( r ); 
        return r.getReturnValue(); 
    }

    void around() : uiAsyncCalls() {
        Runnable r = new Runnable() {
            public void run() {
                try {
                    proceed();
                } catch (Throwable e) {
                    e.printStackTrace();
                    throw new RuntimeException( e ); // Soft the exception ...
                }
            }
        };
        
        getDisplay().asyncExec( r ); 
    }
    
    /**
     * Best effort try for retrieving a valid Display object: we should investigate this stuff
     * deeper ...
     */
    private static Display getDisplay() {
        Display display = Display.getCurrent();
        if (display == null)
            display = PlatformUI.getWorkbench().getDisplay();
        return display;
    }
}
