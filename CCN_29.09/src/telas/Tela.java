package telas;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;

import codigo.Codigo;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Tela extends JFrame {

	protected static String[] Ret_busca = {null,null};

	public static void main(String[] args) {		

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Tela frame = new Tela();		
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	private JTextField textField_Nome;
	private JTextField textField_Venc;
	private JTextField textField_Str_n_page;
	private JTextField textField_Result_Nome;
	protected String[] retb;
	protected boolean FLAG_BUSCA = true;	

	public Tela() {
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent arg0) {
				try {
					Codigo.criaArqProp();
					Codigo.carregaArquivoProp();
					textField_Str_n_page.setText(Codigo.Str_n_page);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
		});

		setTitle("Busca boleto CCN - By Rudsom");
		setResizable(false);

		setBounds(100, 100, 650, 254);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		textField_Nome = new JTextField();
		textField_Nome.setColumns(10);

		textField_Venc = new JTextField();
		textField_Venc.setHorizontalAlignment(SwingConstants.CENTER);
		textField_Venc.setColumns(10);

		textField_Str_n_page = new JTextField();
		textField_Str_n_page.setForeground(Color.RED);
		textField_Str_n_page.setHorizontalAlignment(SwingConstants.CENTER);
		textField_Str_n_page.setColumns(10);

		textField_Result_Nome = new JTextField();
		textField_Result_Nome.setColumns(10);

		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Busca o nome
				Thread th = new Thread() {
					public void run() {
						try {
							//Codigo.criaArqProp();
							Codigo.inicioHtml();
							FLAG_BUSCA = true; //habilita a busca
							boolean FIM = false;							
							while(!FIM && FLAG_BUSCA) {  //enquanto fim nao for verdadeiro e não tiver apertado o btn parar busca
								Ret_busca = Codigo.getNomes(textField_Nome.getText(),textField_Str_n_page.getText());	
								if(Ret_busca[0]=="FIM") FIM=true;
								//mostra os nomes achados
								textField_Result_Nome.setText(Ret_busca[0]);
								textField_Venc.setText(Ret_busca[1]);
								textField_Str_n_page.setText(Ret_busca[2]);
								//grava os arquivos so com os nomes buscados
								//Ret_busca[0] = Ret_busca[0].toUpperCase();
								if(Ret_busca[0].indexOf(textField_Nome.getText().toUpperCase())>0 || Ret_busca[0]=="" ) { //verifica se retornou o nome que tem no campo da busca	
									Codigo.consulta_to_Html();
								}	
							}
							Codigo.fimHtml();	
							Codigo.gravaProp();
							Codigo.abreHtml();

					}catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			};
			th.start();

		}
	});



		JLabel lblNome = new JLabel("Nome a buscar:");

		JLabel lblVencimento = new JLabel("Vencimento:");

		JLabel lblNomes = new JLabel("Resultados:");

		JLabel lblIndice = new JLabel("Indice:");

		JButton btnSair = new JButton("Sair");
		btnSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});

		JButton btnParar = new JButton("Parar");
		btnParar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FLAG_BUSCA = false;
				try {                        //
					Codigo.fimHtml();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} //
			}
		});
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addGap(26)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(textField_Nome, GroupLayout.PREFERRED_SIZE, 421, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblNome))
						.addGap(33)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(textField_Str_n_page, GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
								.addComponent(textField_Venc, GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE))
						.addContainerGap(54, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
						.addGap(480)
						.addComponent(lblVencimento, GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
						.addContainerGap(54, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
						.addGap(480)
						.addComponent(lblIndice, GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
						.addContainerGap(64, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
						.addGap(26)
						.addComponent(lblNomes, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
						.addGap(529))
				.addGroup(groupLayout.createSequentialGroup()
						.addGap(26)
						.addComponent(textField_Result_Nome, GroupLayout.PREFERRED_SIZE, 421, GroupLayout.PREFERRED_SIZE)
						.addGap(197))
				.addGroup(groupLayout.createSequentialGroup()
						.addGap(26)
						.addComponent(btnBuscar)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(btnParar)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(btnSair)
						.addContainerGap(411, Short.MAX_VALUE))
				);
		groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addGap(25)
						.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addGroup(groupLayout.createSequentialGroup()
										.addComponent(lblNome)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(textField_Nome, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGroup(groupLayout.createSequentialGroup()
										.addComponent(lblIndice)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(textField_Str_n_page, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addGap(41)
						.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addGroup(groupLayout.createSequentialGroup()
										.addComponent(lblNomes)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(textField_Result_Nome, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGroup(groupLayout.createSequentialGroup()
										.addComponent(lblVencimento)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(textField_Venc, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addGap(29)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE, false)
								.addComponent(btnBuscar)
								.addComponent(btnParar)
								.addComponent(btnSair))
						.addContainerGap(27, Short.MAX_VALUE))
				);
		groupLayout.linkSize(SwingConstants.HORIZONTAL, new Component[] {btnBuscar, btnSair, btnParar});
		getContentPane().setLayout(groupLayout);

}

public String getIndice() {
	return textField_Str_n_page.getText();
}

public String gets_nome() {
	return textField_Nome.getText();
}

public void sets_nome(String s_nome) {
	textField_Nome.setText(s_nome);
}

public void setVencimento(String html) {
	textField_Venc.setText(html);

}
}
