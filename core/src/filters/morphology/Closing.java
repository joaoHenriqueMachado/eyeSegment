package filters.morphology;

import filters.Filter;
import image.Image;
import morphology.MorphologyConstants;

/**
 * Closing operation from mathematical morphology (dilation + erosion).
 * @author Erick Oliveira Rodrigues (erickr@id.uff.br)
 */
public class Closing extends Filter implements MorphologyConstants{
	
	private Image resultImage = null;
	private Image structuringElement = STRUCT_PRIMARY;
	private int timesToDilate = 3, timesToErode = 3;
	
	/**
	 * Sets the structuring element to be used.
	 * @param structuringElement
	 * @author �rick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setStructuringElement(final Image structuringElement){
		this.structuringElement = structuringElement;
	}
	
	/**
	 * Sets the time that the image should be dilated.
	 * @param timesToDilate
	 * @author �rick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setTimesToDilate(final int timesToDilate){
		this.timesToDilate = timesToDilate;
	}
	
	/**
	 * Sets the time that the image should be eroded.
	 * @param timesToErode
	 * @author �rick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setTimesToErode(final int timesToErode){
		this.timesToErode = timesToErode;
	}

	@Override
	public double getFilteredPixel(Image image, int x, int y, int band) {
		if (resultImage == null){
			Dilation dil = new Dilation(structuringElement, timesToDilate);
			
			resultImage = dil.applyFilter(image);
			
			Erosion ero = new Erosion(structuringElement, timesToErode);
			
			resultImage = ero.applyFilter(resultImage);
		}
		
		return resultImage.getPixel(x, y, band);
	}

	
	public Image applyFilter(final Image image) {
		Image out = super.applyFilter(image);
		this.resultImage = null;
		return out;
	}
}
