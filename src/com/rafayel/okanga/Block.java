package com.rafayel.okanga;

//import java.util.ArrayList;

public class Block {
    /**
     * Block class to simulate namespaces (in the future) and if states for a block
     */
    private Block parrentBlock;
//    private ArrayList<Block> subBlocks;
    private Boolean ifState = true;
    private Boolean skipElse = true;
    private Boolean inElse = false;

    public Boolean getExecuted() {
        return executed;
    }

    public void setExecuted(Boolean executed) {
        this.executed = executed;
    }

    private Boolean executed = false;

    public Boolean getInElse() {
        return inElse;
    }


    public void setInElse(Boolean state) {
        this.inElse = state;
    }


    public Block(Block parrentBlock){
        this.parrentBlock = parrentBlock;
    }

    public Boolean getIfState() {
        return ifState;
    }

    public void setIfState(Boolean state){
        this.ifState = state;
    }

    public Block getParrentBlock(){
        return parrentBlock;
    }

    public Boolean getSkipElse() {
        return skipElse;
    }

    public void setSkipElse(Boolean state){
        this.skipElse = state;
    }

    //
//    public ArrayList<Block> getSubBlocks(){
//        return subBlocks;
//    }
//
//    public void addSubBlock(Block block){
//        subBlocks.add(block);
//    }
}
