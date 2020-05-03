/*******************************************************************************
 * Copyright (c) 2013, Daniel Murphy All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met: * Redistributions of source code must retain the
 * above copyright notice, this list of conditions and the following disclaimer. * Redistributions
 * in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/
package org.jbox2d.testbed.framework.javafx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jbox2d.testbed.framework.*;
import org.jbox2d.testbed.framework.AbstractTestbedController.MouseBehavior;
import org.jbox2d.testbed.framework.AbstractTestbedController.UpdateBehavior;
import org.jbox2d.testbed.framework.javafx.*;
import org.jbox2d.testbed.framework.javafx.PlaySidePanel;

/**
 * The entry point for the testbed application
 *
 * @author Daniel Murphy
 */
public class PlayableMain extends Application {
    @Override
    public void start(Stage primaryStage) {
        PlayModel model = new PlayModel();
        final AbstractTestbedController controller = new PlayControllerJavaFX(model,
                UpdateBehavior.UPDATE_CALLED, MouseBehavior.NORMAL, (Exception e, String message) -> {
            new Alert(Alert.AlertType.ERROR).showAndWait();
        });
        BorderPane testbed = new BorderPane();

        PlayPanelJavaFX panel = new PlayPanelJavaFX(model, controller, testbed);
        model.setPanel(panel);
        model.setDebugDraw(new DebugPlayDrawJavaFX(panel, true));
        LevelsList.populateModel(model);


        testbed.setCenter(panel);

        testbed.setRight(new ScrollPane(new PlaySidePanel(model, controller)));

        Scene scene = new Scene(testbed, TestPanelJavaFX.INIT_WIDTH + 175, TestPanelJavaFX.INIT_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setTitle("JBox2D Testbed");
        primaryStage.show();

        System.out.println(System.getProperty("java.home"));

        Platform.runLater(() -> {
            controller.playTest(2);
            controller.start();
        });
    }
}
