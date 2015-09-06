package com.finki.ukim.mk.mpip.boxlide;

import java.io.IOException;

import org.cocos2d.config.ccMacros;
import org.cocos2d.extensions.scroll.CCClipNode;
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

import android.view.MotionEvent;

public class SlidingMenuLayer extends CCLayer {
	private static final int TILES_NODE_TAG = 300;
	private static CGSize screenSize;
	CCScrollView scrollView;
	CCBitmapFontAtlas statusLabel;
	private CGPoint startlocation; // keep track of touch starting point
	private CGPoint endlocation; // //keep track of touch ending point
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

		for (int i = 0; i < 3; i++) {
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
				PictureGameLayer.getBitmapFromAsset("pacific_rim.jpg");
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
		default:
			break;
		}

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
		for (int i = 0; i < 3; i++) {
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