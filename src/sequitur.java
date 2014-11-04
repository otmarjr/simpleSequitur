/*
 This class is part of a Java port of Craig Nevill-Manning's Sequitur algorithm.
 Copyright (C) 1997 Eibe Frank

 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class sequitur extends java.applet.Applet {

    TextArea text;
    TextArea rules;
    Button submit;
    Panel dataPanel;
    Panel rulesPanel;
    Panel buttonPanel;
    Panel label1Panel;
    Panel label2Panel;
    Label dataLabel;
    Label rulesLabel;
    Font f1 = new Font("TimesRoman", Font.BOLD, 18);
    Font f2 = new Font("TimesRoman", Font.PLAIN, 12);

    public void runSequitur() {

        rule firstRule = new rule();
        int i;

        // Reset number of rules and Hashtable.
        rule.numRules = 0;
        symbol.theDigrams.clear();
        for (i = 0; i < text.getText().length(); i++) {
            firstRule.last().
                    insertAfter(new terminal(text.getText().
                                    charAt(i)));
            firstRule.last().p.check();
        }
        rules.setText(firstRule.getRules());
    }

    public static rule generateRulesForInput(String input) {
        rule firstRule = new rule();

        for (int i = 0; i < input.length(); i++) {
            firstRule.last().
                    insertAfter(new terminal(input.
                                    charAt(i)));
            firstRule.last().p.check();
        }

        return firstRule;
    }

    public static String getGrammarBasedRegex(String input) {
        rule r = generateRulesForInput(input);
        Map<Integer, java.util.List<symbol>> bfs = r.getRulesByBFS();
        Map<Integer, String> rulesResolutions = new HashMap<>();

        String regex = "";

        for (int i = bfs.keySet().size() - 1; i >= 0; i--) {
            java.util.List<Integer> indexesToBeResolved = new LinkedList<>();
            
            java.util.List<symbol> symbols = bfs.get(i);
            
            for (symbol s: symbols){
                if (s.isNonTerminal()){
                    rule rs = ((nonTerminal) s).r;
                    
                    if (rs.index - i > 1){
                        indexesToBeResolved.add(rs.index);
                    }
                }
            }
            
            java.util.List<String> resolvedSymbols = new LinkedList<>();
            
            for (int j = 0;j< symbols.size();j++){
                if (indexesToBeResolved.contains(j)){
                    resolvedSymbols.add(rulesResolutions.get(j));
                }
                else{
                    //resolvedSymbols.add(s
                }
            }
         }

        return regex;
    }

    public static String getSequiturRules(String input) {
        rule firstRule = generateRulesForInput(input);

        Map<Integer, java.util.List<symbol>> bfs;

        StringBuilder sb = new StringBuilder();
        bfs = firstRule.getRulesByBFS();

        bfs.keySet().stream().forEach(rid -> {
            sb.append(rid + "->");
            bfs.get(rid).forEach(s -> {
                sb.append(" ");

                if (s.isNonTerminal()) {
                    sb.append(((nonTerminal) s).r.index);
                } else {
                    sb.append((char) s.value);
                }
            });
        });

        // return firstRule.getRules();
        return sb.toString();
    }

    public boolean action(Event evt, Object arg) {
        if (evt.target == submit) {
            submit.disable();
            text.setEditable(false);
            runSequitur();
            submit.enable();
            text.setEditable(true);
        }
        return true;
    }

    public void init() {

        String defaultText = new String("pease porridge hot,\npease porridge cold,\npease porridge in the pot,\nnine days old.\n\nsome like it hot,\nsome like it cold,\nsome like it in the pot,\nnine days old.\n");

        setLayout(new FlowLayout());
        dataPanel = new Panel();
        dataPanel.setLayout(new BorderLayout());
        add(dataPanel);
        label1Panel = new Panel();
        label1Panel.setLayout(new FlowLayout());
        dataPanel.add("North", label1Panel);
        dataLabel = new Label("Data");
        dataLabel.setFont(f1);
        label1Panel.add(dataLabel);
        text = new TextArea(9, 70);
        text.setFont(f2);
        text.setText(defaultText);
        dataPanel.add("South", text);
        rulesPanel = new Panel();
        rulesPanel.setLayout(new BorderLayout());
        add(rulesPanel);
        label2Panel = new Panel();
        label2Panel.setLayout(new FlowLayout());
        rulesPanel.add("North", label2Panel);
        rulesLabel = new Label("Grammar");
        rulesLabel.setFont(f1);
        label2Panel.add(rulesLabel);
        rules = new TextArea(9, 70);
        rules.setEditable(false);
        rules.setFont(f2);
        rulesPanel.add("South", rules);
        buttonPanel = new Panel();
        buttonPanel.setLayout(new FlowLayout());
        add(buttonPanel);
        submit = new Button("Run sequitur");
        submit.setFont(f1);
        buttonPanel.add(submit);
    }
}
