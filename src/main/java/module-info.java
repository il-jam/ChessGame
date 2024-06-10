/**
 * The main module of the chess application.
 */
module chess {
    requires javafx.controls;
    requires transitive javafx.graphics;
    requires javafx.fxml;
    requires java.desktop;

    exports chess;
    exports chess.model;
    exports chess.gui;
    exports chess.controller;
    opens chess;
}
