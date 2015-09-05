package com.finki.ukim.mk.mpip.boxlide;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.opengl.CCBitmapFontAtlas;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;
import org.cocos2d.utils.CCFormatter;

public class GameLayer extends CCLayer {

	private static final int STATUS_LABEL_TAG = 20;
	private static final int TIMER_LABEL_TAG = 21;
	private static final int MOVES_LABEL_TAG = 22;

	private static int thetime = 0;

	private static CGSize screenSize;
	float generalscalefactor = 0.0f;

	public GameLayer() {
		screenSize = CCDirector.sharedDirector().winSize();
		generalscalefactor = CCDirector.sharedDirector().winSize().height / 500;
		CCSprite background = CCSprite.sprite("background.png");
		background.setScale(screenSize.width
				/ background.getContentSize().width);
		background.setAnchorPoint(CGPoint.ccp(0f, 1f));
		background.setPosition(CGPoint.ccp(0, screenSize.height));
		addChild(background, -5);

		// Add Game Status Label
		/*
		 * CCBitmapFontAtlas statusLabel = CCBitmapFontAtlas.bitmapFontAtlas(
		 * "Tap Tiles to Begin", "magneto.fnt"); statusLabel.setScale(0.8f *
		 * generalscalefactor); // scaled
		 * statusLabel.setAnchorPoint(CGPoint.ccp(0, 1));
		 * statusLabel.setPosition(CGPoint.ccp(25 * generalscalefactor,
		 * screenSize.height - 10 * generalscalefactor)); addChild(statusLabel,
		 * -2, STATUS_LABEL_TAG);
		 */

		// Add Timer Label to track time
		CCBitmapFontAtlas timerLabel = CCBitmapFontAtlas.bitmapFontAtlas(
				"00:00", "magneto.fnt");
		timerLabel.setScale(1.0f * generalscalefactor);
		timerLabel.setAnchorPoint(1f, 1f);
		timerLabel.setColor(ccColor3B.ccc3(195, 54, 77));
		timerLabel.setPosition(CGPoint.ccp(screenSize.width - 200 * generalscalefactor,
				screenSize.height - 60 * generalscalefactor));
		addChild(timerLabel, -2, TIMER_LABEL_TAG);

		// Add Moves Label to track number of moves
		CCBitmapFontAtlas movesLabel = CCBitmapFontAtlas.bitmapFontAtlas(
				"Moves : 000", "magneto.fnt");
		movesLabel.setScale(1.0f * generalscalefactor);
		movesLabel.setAnchorPoint(1f, 0f);
		movesLabel.setColor(ccColor3B.ccc3(195, 54, 77));
		/*
		 * movesLabel.setPosition(CGPoint.ccp(screenSize.width - 25
		 * generalscalefactor, timerLabel.getPosition().y -
		 * timerLabel.getContentSize().height generalscalefactor - 10 *
		 * generalscalefactor - timerLabel.getContentSize().height
		 * generalscalefactor));
		 */
		movesLabel.setPosition(CGPoint.ccp(screenSize.width - 130 * generalscalefactor,
				screenSize.height - 50 * generalscalefactor));
		addChild(movesLabel, -2, MOVES_LABEL_TAG);

		schedule("updateTimeLabel", 1.0f);
	}

	public static CCScene scene() {
		CCScene scene = CCScene.node();
		CCLayer layer = new GameLayer();
		scene.addChild(layer);
		return scene;
	}

	public void updateTimeLabel(float dt) {
		thetime += 1;
		String string = CCFormatter.format("%02d:%02d", (int) (thetime / 60),
				(int) thetime % 60);
		CCBitmapFontAtlas timerLabel = (CCBitmapFontAtlas) getChildByTag(TIMER_LABEL_TAG);
		timerLabel.setString(string);
	}
}
