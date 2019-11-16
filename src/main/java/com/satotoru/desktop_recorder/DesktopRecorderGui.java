package com.satotoru.desktop_recorder;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

public class DesktopRecorderGui extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JButton recordButton;
	private JLabel outputFileLabel;
	private JButton fileSelectionButton;
	private String outputFile = "capture";
	private boolean recording;
	private DesktopRecorder recorder;

	public DesktopRecorderGui() {
		super("Desktop Recorder");

		setLayout(new BorderLayout());
		JPanel mainPanel = new JPanel();
		add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 8));

		this.recordButton = new JButton("Start");
		this.recordButton.setActionCommand("RECORD");
		this.recordButton.addActionListener(this);
		mainPanel.add(this.recordButton);
		this.outputFileLabel = new JLabel(getOutputFileLabelString());
		this.outputFileLabel.setPreferredSize(new Dimension(200, 30));
		mainPanel.add(this.outputFileLabel);
		this.fileSelectionButton = new JButton("Select");
		this.fileSelectionButton.setActionCommand("SELECT");
		this.fileSelectionButton.addActionListener(this);
		mainPanel.add(this.fileSelectionButton);

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}
		setSize(400, 90);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	private String getOutputFileLabelString() {
		return "Save to " + this.outputFile + ".mp4";
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("RECORD")) {
			if (!this.recording) {
				this.recordButton.setText("Stop");
				this.fileSelectionButton.setEnabled(false);
				this.recording = true;
				try {
					this.recorder = new DesktopRecorder(this.outputFile);
					this.recorder.start();
				} catch (Exception ex) {
				}
			} else {
				this.recordButton.setText("Start");
				this.fileSelectionButton.setEnabled(true);
				this.recording = false;
				try {
					this.recorder.stop();
				} catch (Exception ex) {
				}
			}
		} else if (e.getActionCommand().equals("SELECT")) {
			String curretDir = new File(this.outputFile).getParent();
			JFileChooser chooser = new JFileChooser(curretDir == null ? "." : curretDir);
			chooser.setSelectedFile(new File(this.outputFile + ".mp4"));
			chooser.setFileFilter(new FileNameExtensionFilter("MP4 File", "mp4"));
			if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				try {
					this.outputFile = chooser.getSelectedFile().getCanonicalPath().replaceFirst(".mp4$", "");
					this.outputFileLabel.setText(getOutputFileLabelString());
				} catch (Exception ex) {
				}
			}
		}
	}

	public static void main(String[] args) {
		DesktopRecorderGui gui = new DesktopRecorderGui();
	}

}
