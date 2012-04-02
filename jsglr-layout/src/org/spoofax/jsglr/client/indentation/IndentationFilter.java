package org.spoofax.jsglr.client.indentation;

import java.util.Stack;

import org.spoofax.jsglr.client.AbstractParseNode;
import org.spoofax.jsglr.client.ParseTable;
import org.spoofax.jsglr.client.Production;
import org.spoofax.jsglr.client.ProductionAttributes;

/**
 * @author Sebastian Erdweg <seba at informatik uni-marburg de>
 */
public class IndentationFilter {
  
//  public static boolean hasValidIndentation(Production prod, AbstractParseNode[] kids, ParseTable tbl) {
//    if (kids.length <= 0)
//      return true;
//    
//    int indentation = -1;
//    
//    ProductionAttributes attr = tbl.getLabel(prod.label).getAttributes();
//
//    for (AbstractParseNode node : kids) {
//      if (node.isParseProductionNode())
//        continue;
//      
//      if (indentation == -1)
//        indentation = node.getIndentation();
//      
//      if (!node.isAmbNode() && tbl.getLabel(node.getLabel()).isLayout() || node.isEmpty())
//        continue;
//      
//      if (attr.isSameIndent() && node.getIndentation() != indentation)
//        return false;
//      
//      if (!node.isAmbNode() && tbl.getLabel(node.getLabel()).getAttributes().isMoreIndent() && node.getIndentation() <= indentation)
//        return false;
//    }
//    
//    if (!attr.isOffsideBlock())
//      return true;
//    
//    return hasValidOffsideIndentation(kids, tbl);
//  }
//  
//  public static boolean hasValidOffsideIndentation(AbstractParseNode node, ParseTable tbl) {
//    return hasValidOffsideIndentation(new AbstractParseNode[] {node}, tbl);
//  }
//  
//  private static boolean hasValidOffsideIndentation(AbstractParseNode[] kids, ParseTable tbl) {
//    Stack<AbstractParseNode> stack = new Stack<AbstractParseNode>();
//    
//    for (int i = kids.length - 1; i >= 0; i--)
//      stack.push(kids[i]);
//    
//    int minTargetIndentation = -1;
//    boolean readToken = false;
//    
//    while (!stack.isEmpty()) {
//      AbstractParseNode node = stack.pop();
//      
//      if (node.isParseProductionNode()) {
//        readToken = true;
//        continue;
//      }
//      
//      if (minTargetIndentation == -1)
//        minTargetIndentation = node.getIndentation();
//      
//      if (!node.isAmbNode() && tbl.getLabel(node.getLabel()).isLayout() || node.isEmpty())
//        continue;
//      
//      if (!node.isAmbNode() && tbl.getLabel(node.getLabel()).getAttributes().isIgnoreIndent()) {
//        if (!readToken)
//          readToken = !node.isEmpty();
//        continue;
//      }
//      
//      if (readToken && node.getIndentation() <= minTargetIndentation ||
//         !readToken && node.getIndentation() <  minTargetIndentation)
//        return false;
//      
//      if (!node.isAmbNode() &&
//          tbl.getLabel(node.getLabel()).getAttributes().isOffsideBlock() &&
//          node.getIndentation() > minTargetIndentation) {
//        if (!readToken)
//          readToken =!node.isEmpty();
//        continue;
//      }
//      
//      if (node.isAmbNode()) {
//        stack.push(node.getChildren()[0]);
//        continue;
//      }
//      
//      for (int i = node.getChildren().length - 1; i >= 0; i--)
//        stack.push(node.getChildren()[i]);
//    }
//    
//    
//    return true;
//  }
}
