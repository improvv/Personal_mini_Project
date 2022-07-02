import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class CoinEat extends JFrame{
	private Image bufferImage;
	private Graphics screenGraphic;
	
	private Clip clip;
	
	private Image backgroundImage = new ImageIcon("src/images/mainScreen.png").getImage();	//이미지
	private Image player = new ImageIcon("src/images/player.png").getImage();
	private Image coin = new ImageIcon("src/images/coin.png").getImage();
	private Image black = new ImageIcon("src/images/black.png").getImage();
	
	private int playerX, playerY;	//player좌표
	private int playerWidth = player.getWidth(null);		//palyer와 coin의 충돌 여부 판단위해 각 이미지 크기 담기
	private int playerHeight = player.getHeight(null);
	private int coinX, coinY;	
	private int coinWidth = player.getWidth(null);
	private int coinHeight = player.getHeight(null);
	
	private int score;		//player점수
	
	private boolean up, down, left, right;
	
	public CoinEat() {
		setTitle("동전 먹기 게임");		//제목
		setVisible(true);		//보이기 여부 
		setSize(500, 500);		//창크기
		setLocationRelativeTo(null);	//null입력시 실행시 창이 화면 가운데 실행
		setResizable(false);	//창크기조절가능여부
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {	//key를 눌렀을때 실행 메서드
				switch(e.getKeyCode()) {
				case KeyEvent.VK_UP:	//위
					up = true;
					break;
				case KeyEvent.VK_DOWN:	//아래
					down = true;
					break;
				case KeyEvent.VK_LEFT:	//왼
					left = true;
					break;
				case KeyEvent.VK_RIGHT:	//오
					right = true;
					break;
				}
			}
			
			public void keyReleased(KeyEvent e) {	//key를 뗏을때 실행 메서드
				switch(e.getKeyCode()) {
				case KeyEvent.VK_UP:
					up = false;
					break;
				case KeyEvent.VK_DOWN:
					down = false;
					break;
				case KeyEvent.VK_LEFT:
					left = false;
					break;
				case KeyEvent.VK_RIGHT:
					right = false;
					break;
				}
			}
		});
		Init();	//게임 초기화
		while(true) {	//반복
			try {
				Thread.sleep(20);	//대기시간
				if(score==1000) {	//score가 1000일때 초기화
					Init();
				}
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
			keyProcess();
			crashCheck();
		}
	}
	
	public void Init() {		//초기화 메서드
		score = 0;
		playerX = (500 - playerWidth)/2;	//정중앙으로 오게 설정
		playerY = (500 - playerHeight)/2;
		
		coinX = (int)(Math.random()*(501-playerWidth));		//coin위치는 random
		coinY = (int)(Math.random()*(501-playerHeight-30))+30;
		
		playSound("src/audio/backgroundMusic.wav", true);
	}
	
	public void keyProcess() {
		if(up && playerY - 3 > 30) playerY-=3;
		if(down && playerY + playerHeight + 3 < 500) playerY+=3;
		if(left && playerX - 3 > 0) playerX-=3;
		if(right && playerX + playerWidth + 3 < 500) playerX+=3;
	}
	
	public void paint(Graphics g) {		//깜빡임 방지
		bufferImage = createImage(500, 500);
		screenGraphic = bufferImage.getGraphics();
		screenDraw(screenGraphic);
		g.drawImage(bufferImage, 0, 0, null);
	}
	
	public void crashCheck() {	//player와 coin이 닿았을 때
		if(playerX + playerWidth > coinX && coinX + coinWidth > playerX && playerY + playerHeight > coinY && coinY + coinHeight > playerY) {
			score+=100;		//점수 추가
			playSound("src/audio/getCoin.wav", false);
			coinX = (int)(Math.random()*(501-playerWidth));		//위치 옮김
			coinY = (int)(Math.random()*(501-playerHeight-30))+30;
		}
	}
	
	public void playSound(String pathName, boolean isLoop) {	//사운드 생성, 무한루프
		try {
			clip = AudioSystem.getClip();
			File audioFile = new File(pathName);
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
			clip.open(audioStream);
			clip.start(); 		//오디오 재생
			if(isLoop)
				clip.loop(Clip.LOOP_CONTINUOUSLY);
		}catch(LineUnavailableException e) {
			e.printStackTrace();
		}catch(UnsupportedAudioFileException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void screenDraw(Graphics g) {		//화면에 표시
		g.drawImage(backgroundImage, 0, 0, null);
		g.drawImage(coin, coinX, coinY, null);
		g.drawImage(player, playerX, playerY, null);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 40));
		g.drawString("SCORE : "+score, 150, 80);
		this.repaint();
	}

	public static void main(String[] args) {
		new CoinEat();	//생성자 호출
		

	}

}
