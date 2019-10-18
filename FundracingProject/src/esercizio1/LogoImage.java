package esercizio1;

import javafx.scene.image.ImageView;

public class LogoImage {
	
	 private ImageView image;

	 LogoImage(ImageView img) {
	        this.image = img;
	    }

	    public void setImage(ImageView value) {
	        image = value;
	    }

	    public ImageView getImage() {
	        return image;
	    }

}
