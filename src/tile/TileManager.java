package tile;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.UtilityTool;

public class TileManager {
	GamePanel gp;
	public Tile [] tile;
	public int mapTileNum[] [];
	public TileManager (GamePanel gp) {
		this.gp = gp;
		tile = new Tile[50];
		mapTileNum = new int[gp.maxWorldCol] [gp.maxWorldRow];
		getTileImage();
		loadMap("/maps/map_output.txt");
	}
	public void getTileImage() {
			
		setup(1, "final00", false);
		setup(2, "final01", false);
		setup(3, "final02", true);
		setup(4, "final03", true);
		setup(5, "final04", true);
		setup(6, "final05", false);
		setup(7, "final06", false);
		setup(8, "final07", false);
		setup(9, "final08", false);
		setup(10, "final09", false);
		setup(11, "final10", false);
		setup(12, "final11", false);
		setup(13, "final12", false);
		setup(14, "final13", false);
		setup(15, "final14", false);
		setup(16, "final15", false);
		setup(17, "final16", false);
		setup(18, "final17", false);
		setup(19, "final18", false);
		setup(20, "final19", false);
		setup(21, "final20", false);
		setup(22, "final21", false);
		setup(23, "final22", false);
		setup(24, "final23", false);
		setup(25, "final24", false);
		setup(26, "final25", false);
		setup(27, "final26", false);
		setup(28, "final27", false);
		setup(29, "final28", true);
		setup(30, "final29", true);
		setup(31, "final30", true);
		setup(32, "tree", true);		 	
		
	}
	
	public void setup(int index, String imageName, boolean collision) {
		UtilityTool uTool = new UtilityTool();
		try {
			
			tile[index] = new Tile();
			tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/" + imageName +".png"));
			tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
			tile[index].collision = collision;	
		} catch(IOException e) {
			e.printStackTrace();
		}	
	}
	public void loadMap(String filePath) {
		
		try {
			InputStream is = getClass().getResourceAsStream(filePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			int col = 0;
			int row= 0;
			
			while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
				String line = br.readLine();
				
				while (col < gp.maxWorldCol) {
					String numbers[] = line.split(" ");	
					int num = Integer.parseInt((numbers[col]));
					mapTileNum[col] [row] = num;
					col++;
				}
				if(col == gp.maxWorldCol) {
					col = 0;
					row++;
				}
			}
			br.close();	
		} catch (Exception e) {
		}
	}
	public void draw(Graphics2D g2) {
		
		int worldCol = 0;
		int worldRow = 0;
		while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
			
			int tileNum = mapTileNum [worldCol] [worldRow];
			int worldX = worldCol * gp.tileSize;
			int worldY= worldRow * gp.tileSize;
			int screenX = worldX - gp.player.worldX + gp.player.screenX;
			int screenY = worldY - gp.player.worldY + gp.player.screenY;
			
			if (worldX + gp.tileSize> gp.player.worldX - gp.player.screenX && 
				worldX - gp.tileSize< gp.player.worldX + gp.player.screenX && 
				worldY + gp.tileSize> gp.player.worldY - gp.player.screenY && 
				worldY - gp.tileSize< gp.player.worldY + gp.player.screenY) {
				g2.drawImage(tile[tileNum].image, screenX, screenY, null);
			}
			worldCol ++;
			if(worldCol == gp.maxWorldCol) {
				worldCol = 0;
				worldRow ++;	
			}
		}	
	}
}