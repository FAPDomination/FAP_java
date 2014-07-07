package npcs;

import fap_java.Graph;
import fap_java.NPC;
import fap_java.Tools;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;


public class NPCWMBlocking extends NPC {
    @SuppressWarnings("compatibility:5909433600382951811")
    private static final long serialVersionUID = -1529764395083968508L;

    public NPCWMBlocking(String cellHash) {
        super(cellHash, false, false, null, (int)Graph.getOffsetsCells().get(0).getWidth() + 1,
              (int)(Graph.getOffsetsCells().get(0).getWidth() - 8), null);
        //TODO Update image and override paintComponent
        img = "basicCellImage";
    }

    public void paintComponent(Graphics g) {
        Image bild = Graph.getList().get(img);
        double width = (bild.getWidth(game) * Graph.getFacW());
        double height = (bild.getHeight(game) * Graph.getFacH());
        double scaleX = width / bild.getWidth(game);
        double scaleY = height / bild.getWidth(game);
        float[] offsets = new float[4];
        float[] scales = { 0.1f, 0.1f, 0.1f, (float)0.6 };
        Tools.drawFilteredImage((BufferedImage)bild, scales, offsets, g, x, y, scaleX, scaleY);
    }

    public void execute() {
    };

}
