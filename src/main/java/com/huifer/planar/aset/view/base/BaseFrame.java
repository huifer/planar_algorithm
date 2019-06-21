package com.huifer.planar.aset.view.base;

import java.awt.EventQueue;
import javax.swing.JFrame;

/**
 * <p>Title : BaseFrame </p>
 * <p>Description : 基础显示框架</p>
 *
 * @author huifer
 * @date 2019-01-15
 */
public class BaseFrame extends JFrame {

    private FrameContext frameContext;

    public BaseFrame(FrameContext frameContext) {
        this.frameContext = frameContext;
        init();
    }

    private void init() {
        add(frameContext);
        setTitle("显示框");
        setSize(1024, 768);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void run() {
        EventQueue.invokeLater(
                () -> {
                    BaseFrame ex = new BaseFrame(frameContext);
                    ex.setVisible(true);
                }
        );
    }
}
