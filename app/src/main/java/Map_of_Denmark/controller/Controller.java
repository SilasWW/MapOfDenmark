package Map_of_Denmark.controller;

import Map_of_Denmark.model.Model;
import Map_of_Denmark.view.DebugView;
import Map_of_Denmark.view.View;

/**
 * Handles most basic user-interactions. specific UI-elements handle their own interactions
 */
public class Controller {
    private double lastX;
    private double lastY;
    
    private long lastClick;

    /**
     * Constructor for the Controller class (controller layer of MVC)
     * @param model The model layer of MVC as argument (from the model.Model class)
     * @param view The view layer of MVC as argument (from the view.View class)
     */
    public Controller(Model model, View view) {

        //Saves the x,y coordinates of the mouse relative to the canvas. To be used later when mousedragged
        view.canvas.setOnMousePressed(e -> {
            lastX = e.getX();
            lastY = e.getY();
        });

        /*
        When the mouse is dragged and primarybutton is down we pan by the difference between current mouse coordinates
        and the last known mouse coordinates in relation to the canvas.
        If the primarybutton is not down while dragging we update lastX and lastY.
         */
        view.canvas.setOnMouseDragged(e -> {
            if (e.isPrimaryButtonDown()) {
                double dx = e.getX() - lastX;
                double dy = e.getY() - lastY;
                view.pan(dx, dy);
            }

            lastX = e.getX();
            lastY = e.getY();
        });

        /*
        When the mouse is scrolled factor variable is assigned to deltaY of the scroll. We them zoom around the
        mouse-position with the factor variable.
         */
        view.canvas.setOnScroll(e -> {
            double factor = e.getDeltaY();
            view.zoom(e.getX(), e.getY(), Math.pow(1.01, factor));
        });

        //updates the debug window with relevant information on the mouse-movement
        view.canvas.setOnMouseMoved(event -> {
            DebugView.getInstance().updateCoords(event.getX(), event.getY(), view.mousetoModel(event.getX(), 0).getX() * 0.56, view.mousetoModel(0, event.getY()).getY());
        });

        //on double clicks addPOI is called to create a new point of interest on the location of the two clicks
        view.canvas.setOnMouseClicked(e -> {
        	if(System.currentTimeMillis() - lastClick < 500)
        		view.addPOI(view.mousetoModel(e.getX(), e.getY()).getX() - 0.00006, view.mousetoModel(e.getX(), e.getY()).getY() - 0.00006);
        	else
        		lastClick = System.currentTimeMillis();
        });
    }
}
