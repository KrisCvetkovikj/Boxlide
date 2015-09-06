package com.finki.ukim.mk.mpip.boxlide;

import org.cocos2d.nodes.CCNode;

public class CCNodeExt extends CCNode {
	public String nodeText;

	public CCNodeExt() {
		super();
	}

	public void setNodeText(String nText) {
		this.nodeText = nText;
	}

	public String getNodeText() {
		return this.nodeText;
	}
}
