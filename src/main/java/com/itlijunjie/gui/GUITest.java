package com.itlijunjie.gui;

import com.alibaba.fastjson.JSON;
import com.itlijunjie.entity.ResultEntity;
import com.itlijunjie.pt.fbs.FlatBuffersResult;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.List;

public class GUITest {
    public GUITest() {
        textLabel.setOpaque(true);
        textLabel.setBackground(Color.PINK);
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String url = "http://172.16.20.75:8080/pt/call/flatbuffers/Demo1/list";
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                Call call = okHttpClient.newCall(request);
                try {
                    Response response = call.execute();
                    String bs = response.body().string();
                    ByteBuffer bb = ByteBuffer.wrap(bs.getBytes());
                    FlatBuffersResult fbr = FlatBuffersResult.getRootAsFlatBuffersResult(bb);
                    List<ResultEntity> entityList = JSON.parseArray(fbr.res(), ResultEntity.class);
                    GUITest.this.disposeData(entityList);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("标题");
        frame.setContentPane(new GUITest().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(400, 400);
        frame.setVisible(true);
    }

    private JPanel panel1;
    private JButton button1;
    private JLabel textLabel;
    private JLabel imageLabel;

    private void disposeData(List<ResultEntity> entities) {
        for (ResultEntity entity : entities) {
            System.out.println(entity.getText());
            System.out.println(entity.getImage());
            textLabel.setText(entity.getText());
            try {
                imageLabel.setIcon(new ImageIcon(new URL(entity.getImage())));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            break;
        }
    }

}
