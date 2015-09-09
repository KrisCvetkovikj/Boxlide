package com.finki.ukim.mk.mpip.boxlide;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Random;

import org.cocos2d.config.ccMacros;
import org.cocos2d.extensions.scroll.CCScrollView;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCNode;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.opengl.CCBitmapFontAtlas;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;
import org.cocos2d.types.CGSize;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.PreferenceManager;
import android.view.MotionEvent;

public class SlidingMenuLayer extends CCLayer {
	private static final int TILES_NODE_TAG = 300;
	private static CGSize screenSize;
	CCScrollView scrollView;
	CCBitmapFontAtlas statusLabel;
	private CGPoint startlocation; // keep track of touch starting point
	float tilescale;

	float generalscalefactor = 0.0f;

	public SlidingMenuLayer() {

		this.setIsTouchEnabled(true);
		this.setIsKeyEnabled(true);
		this.isTouchEnabled_ = true;

		screenSize = CCDirector.sharedDirector().winSize();
		generalscalefactor = CCDirector.sharedDirector().winSize().height / 500;

		CCSprite background = CCSprite.sprite("background.jpg");
		background.setScale(screenSize.width
				/ background.getContentSize().width);
		background.setAnchorPoint(CGPoint.ccp(0f, 1f));
		background.setPosition(CGPoint.ccp(0, screenSize.height));
		addChild(background, -5);

		// Create our menu titles

		CCSprite tilebox;
		tilescale = 1.5f * generalscalefactor;

		CCNode tilesNode = CCNode.node();
		tilesNode.setTag(TILES_NODE_TAG);
		addChild(tilesNode);

		for (int i = 0; i < 4; i++) {
			// A meu image sprite
			tilebox = CCSprite.sprite("menu" + (i + 1) + ".png");
			tilebox.setAnchorPoint(0.5f, 0.5f);
			tilebox.setScale(tilescale * 0.8f);

			// Each one is placed with a 30 pixel space from the next ... i
			// *30*generalscalefactor.
			tilebox.setPosition(CGPoint
					.ccp(screenSize.width / 2,
							screenSize.height
									- (screenSize.height / 3)
									- (i * (screenSize.height / 8) * generalscalefactor)));

			tilesNode.addChild(tilebox, 1, i);
		}

		// You need to set contentSize to enable scrolling.

		// The scrollview is like a sliding window that shows portions of a long
		// list
		// View Size is the size of the window and Content size is the size of
		// the entire long list
		// For scrolling to occur, the content lenght must be greater than the
		// viewsize
		// Thus you must set both content and view size.

	}

	public static CCScene scene() {
		CCScene scene = CCScene.node();
		CCLayer layer = new SlidingMenuLayer();
		scene.addChild(layer);
		return scene;
	}

	// Controls the events launched when a menu item is clicked
	private void launchmenu(int i) {

		CCScene scene;

		switch (i) {
		case 0:
			try {

				File f = new File("images/");
				File[] listOfFiles = f.listFiles();
				String[] imageNames = { "images/wallhaven121852.jpg",
						"images/wallhaven152578.jpg",
						"images/wallhaven161682.jpg",
						"images/wallhaven162495.jpg",
						"images/wallhaven174368.jpg",
						"images/wallhaven175114.jpg",
						"images/wallhaven230769.jpg",
						"images/wallhaven37106.jpg",
						"images/wallhaven60979.jpg",
						"images/wallhaven65974.jpg" };

				PictureGameLayer
						.getBitmapFromAsset(imageNames[new Random().nextInt(10)]);
				// PictureGameLayer.getBitmapFromAsset(listImages[new
				// Random().nextInt(10)]);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			scene = PictureGameLayer.scene(); //
			CCDirector.sharedDirector().runWithScene(scene);
			break;
		case 1:
			PictureGameLayer.getBitMapfromCamera();
			break;
		case 2:
			PictureGameLayer.getBitMapFromGallery();
			break;
		case 3:
			showHighscores();
		default:
			break;
		}

	}

	public void showHighscores() {
		Context appcontext = CCDirector.sharedDirector().getActivity();
		Singleton.getInstance().highscore = PreferenceManager
				.getDefaultSharedPreferences(appcontext).getInt("highscore",
						Singleton.getInstance().highscore);
		int highscore = Singleton.getInstance().highscore;

		final StringBuilder viewHS = new StringBuilder("CURRENT HIGHSCORE");
		viewHS.append("\n\n");
		if (highscore == Integer.MAX_VALUE) {
			viewHS.append("No current highscore.");
		} else {
			viewHS.append(String.format("%02d:%02d", (int) highscore / 60,
					(int) highscore % 60));
		}
		ccMacros.CCLOG("hs", String.format("%02d:%02d", (int) highscore / 60,
				(int) highscore % 60));

		CCDirector.sharedDirector().getActivity().runOnUiThread(new Runnable() {
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						CCDirector.sharedDirector().getActivity());
				builder.setMessage(viewHS.toString())
						.setCancelable(false)
						.setPositiveButton("Clear",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										Context appcontext = CCDirector
												.sharedDirector().getActivity();
										PreferenceManager
												.getDefaultSharedPreferences(
														appcontext)
												.edit()
												.putInt("highscore",
														Integer.MAX_VALUE)
												.commit();
										dialog.cancel();
									}
								})
						.setNegativeButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
									}
								});
				AlertDialog alert = builder.create();
				alert.show();
			}
		});
	}

	@Override
	public boolean ccTouchesBegan(MotionEvent event) {

		startlocation = CCDirector.sharedDirector().convertToGL(
				CGPoint.ccp(event.getX(), event.getY()));

		// ccMacros.CCLOG("Dist Bdgan ","------------------ " );

		return true;
	}

	@Override
	public boolean ccTouchesEnded(MotionEvent event) {
		// Get touch location cordinates
		CGRect spritePos;

		CCNode tilesNode = (CCNode) getChildByTag(TILES_NODE_TAG);

		// We loop through each of the tiles and get its cordinates
		for (int i = 0; i < 4; i++) {
			CCSprite eachNode = (CCSprite) tilesNode.getChildByTag(i);

			// we construct a rectangle covering the current tiles cordinates
			spritePos = CGRect.make(
					eachNode.getPosition().x
							- (eachNode.getContentSize().width
									* generalscalefactor / 2.0f),
					eachNode.getPosition().y
							- (eachNode.getContentSize().height
									* generalscalefactor / 2.0f),
					eachNode.getContentSize().width * generalscalefactor,
					eachNode.getContentSize().height * generalscalefactor);
			// Check if the user's touch falls inside the current tiles
			// cordinates
			if (spritePos.contains(startlocation.x, startlocation.y)) {
				// ccMacros.CCLOG("Began Touched Node", "Began touched : " +
				// eachNode.getNodeText());
				launchmenu(i);

			}
		}

		return true;
	}
}