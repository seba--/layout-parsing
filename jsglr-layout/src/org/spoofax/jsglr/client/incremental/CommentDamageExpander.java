package org.spoofax.jsglr.client.incremental;

import static java.lang.Math.max;
import static org.spoofax.jsglr.client.incremental.IncrementalSGLR.DEBUG;

/**
 * A helper class that expands the damage region
 * for incremental parsing if block or line comments
 * are involved.
 * 
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class CommentDamageExpander {
	
	public static final CommentDamageExpander C_STYLE = new CommentDamageExpander("/*", "*/");
	
	public static final CommentDamageExpander ONLY_LINE_COMMENTS = new CommentDamageExpander(null, null);

	private final String commentStart;
	
	private final String commentEnd;

	public CommentDamageExpander(String commentStart, String commentEnd) {
		this.commentStart = commentStart;
		this.commentEnd = commentEnd;
	}
	
	public int getExpandedDamageStart(String newInput, int damageStart, int damageEnd, int damageSizeChange) {
		// Move to preceding newline, in case line comments are involved
		return newInput.lastIndexOf('\n', damageStart - 1) + 1;
	}
	
	/**
	 * Expands the damage region end offset to accommodate
	 * for unclosed comment blocks and line comments.
	 */
	public int getExpandedDamageEnd(String newInput, int damageStart, int damageEnd, int damageSizeChange) {
		// Move back to '/' if user just entered '*'
		damageStart = commentStart == null ? damageStart : max(0, damageStart - commentStart.length() + 1);
		// Move to following newline, in case line comments are involved
		damageEnd = max(damageEnd, newInput.indexOf('\n', damageEnd + damageSizeChange) - damageSizeChange);
		
		if (commentStart == null) return damageEnd; 
		
		int commentStartOffset = newInput.lastIndexOf(commentStart, damageEnd + damageSizeChange);
		int commentEndOffset = newInput.indexOf(commentEnd, commentStartOffset + commentStart.length() + 1);
		if (commentEndOffset == -1) {
			if (DEBUG && commentStartOffset != -1)
				System.out.println("Unterminated comment ignored"); // try to recover locally
			return damageEnd;
		} else if (commentEndOffset + commentEnd.length() <= damageStart) {
			// Comment found, but was closed before damage region
			return damageEnd;
		} else {
			assert commentEndOffset - damageSizeChange + commentEnd.length() >= damageEnd;
			return commentEndOffset - damageSizeChange + commentEnd.length();
		}
			
	}
}
