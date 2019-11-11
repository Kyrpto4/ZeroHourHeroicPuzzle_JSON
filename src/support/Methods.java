package support;

import java.awt.BorderLayout;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.ListIterator;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import org.apache.commons.compress.utils.IOUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties
@SuppressWarnings("unused")
public class Methods {
	public String theFile = "";
	public String fileName = "";
	public String weeklyConfig = "";
	public ObjectMapper objectMapper = new ObjectMapper();
	List<Terminal> answerSheet = null;

	public void builder(String oneLeft, String oneRight, String threeLeft, String threeRight) throws IOException {
		String firstCall = oneLeft + "-" + oneRight;
		String secondCall = threeLeft + "-" + threeRight;
		analyzer(firstCall, secondCall);
	}

	public void analyzer(String arg1, String arg2) throws JsonParseException, JsonMappingException, IOException {
		ListIterator<Terminal> it = answerSheet.listIterator();
		while (it.hasNext()) {
			String ans = "";
			Terminal temp = it.next();
			if (temp.getTerm1().equals(arg1) && temp.getTerm3().equals(arg2)) {
				ans = temp.getAnswer();
				int result = JOptionPane.showConfirmDialog(null, "The terminal is: \n" + ans, "Terminal",
						JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.CANCEL_OPTION) {
					System.exit(0);
				}
			}
		}
	}

	public void comboInput() throws IOException {
		String[] labels = { "Terminal 1 - Left", "Terminal 1 - Right", "Terminal 3 - Left", "Terminal 3 - Right" };
		JTextField[] inputs = { new JTextField(3), new JTextField(3), new JTextField(3), new JTextField(3) };
		JComponent labelsAndFields = LayoutMaker.getTwoColumnLayout(labels, inputs);
		JComponent spellPanel = new JPanel(new BorderLayout(5, 5));
		spellPanel.add(new JLabel(weeklyConfig, SwingConstants.CENTER), BorderLayout.PAGE_START);
		spellPanel.add(labelsAndFields, BorderLayout.CENTER);
		int result = JOptionPane.showConfirmDialog(null, spellPanel, "Enter Combination", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			builder(inputs[0].getText(), inputs[1].getText(), inputs[2].getText(), inputs[3].getText());
			comboInput();
		} else {
			System.exit(0);
		}
	}

	public void initiate() throws Exception {
		Object[] possibilities = { "Arc", "Solar", "Void", "Exit" };
		Object input = JOptionPane.showOptionDialog(null, "Which configuration for this week?", "Start Menu",
				JOptionPane.PLAIN_MESSAGE, 3, UIManager.getIcon("OptionPane.questionIcon"), possibilities, "Start");
		String option = input.toString();
		String config = "";
		int pic = 0;
		switch (option) {
		case "0":
			config = "Arc";
			pic = 3;
			break;
		case "1":
			config = "Solar";
			pic = 4;
			break;
		case "2":
			config = "Void";
			pic = 5;
			break;
		default:
			System.exit(0);
		}
		weeklyConfig = config;
		fileName = "/" + config + ".json";
		try {
			InputStream inp = getClass().getResourceAsStream(fileName);
			String jsonArray = new String(IOUtils.toByteArray(inp), "UTF-8");
			answerSheet = objectMapper.readValue(jsonArray, new TypeReference<List<Terminal>>() {
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
