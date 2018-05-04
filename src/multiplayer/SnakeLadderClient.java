package multiplayer;

import java.io.IOException;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import gameLogic.BackwardSquare;
import gameLogic.Board;
import gameLogic.Die;
import gameLogic.FreezeSquare;
import gameLogic.Ladder;
import gameLogic.Piece;
import gameLogic.Player;
import gameLogic.Snake;
import gameLogic.Square;

public class SnakeLadderClient {

	private Client client;
	private Player player;
	private Player currentPlayer;
	private String status;
	
	private SnakeAndLadderUI scu;

	public SnakeLadderClient() throws IOException {
		client = new Client();

		client.getKryo().register(Board.class);
		client.getKryo().register(Piece.class);
		client.getKryo().register(Square.class);
		client.getKryo().register(Die.class);
		client.getKryo().register(BackwardSquare.class);
		client.getKryo().register(FreezeSquare.class);
		client.getKryo().register(Snake.class);
		client.getKryo().register(Ladder.class);

		client.getKryo().register(Player.class);
		client.getKryo().register(GameData.class);
		client.getKryo().register(RollData.class);
		
		client.addListener(new clientListener());
		
		new Thread() {
			@Override
			public void run() {
				javafx.application.Application.launch(SnakeAndLadderUI.class);
			}
		}.start();
		scu = SnakeAndLadderUI.waitForLaunch();
		scu.setClient(this);
		
		client.start();
		client.connect(5000, "127.0.0.1", 50000);
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public String getStatus() {
		return status;
	}

	class clientListener extends Listener {
		@Override
		public void connected(Connection arg0) {
			super.connected(arg0);
			System.out.println("Connected!!");
		}

		@Override
		public void disconnected(Connection arg0) {
			super.disconnected(arg0);
		}

		@Override
		public void received(Connection arg0, Object arg1) {
			super.received(arg0, arg1);
			System.out.println("receive");
			System.out.println(arg1);
			if (arg1 instanceof GameData) {
				GameData data = (GameData) arg1;
				if (data.getStatus().equals("Start")) {
					player = data.getCurrentPlayer();
					status = data.getStatus();
					scu.setPlayer(player);
				} else {
					currentPlayer = data.getCurrentPlayer();
					if(currentPlayer.getName().equals(player.getName())) {
						scu.setCurrentPlayer(currentPlayer);
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		try {
			SnakeLadderClient snc = new SnakeLadderClient();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
