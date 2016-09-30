package codigo;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;  

public class Codigo {

	
	//public static final String[] Ret_Busca = null;
	//pega o endereco diretorio documents do usuario
	private static String raiz_url = "http://www.sysca.com.br/ver_boleto.php?cod=";
	private static String pathProp = System.getProperty("user.home") + "\\Documents" + "\\BoletoCCN.properties";
	private static String myDocumentPath = System.getProperty("user.home") + "\\Documents" + "\\BoletoCCN.html";
    public static int n_page = 52000;
	public static String Str_n_page = null;
	//Grava arquivo de configuracao prop
	static Properties prop = new Properties();
	static File fProp = new File(pathProp);
	private static FileWriter arquivo;
	private static PrintWriter grava_arq;
	private static String s_nome;
	public static String[] Ret_busca = {"","",""};
	private static int n = 0;
	private static boolean FLAG_LOOP = false;
	
	public static boolean isFLAG_LOOP() {
		return FLAG_LOOP;
	}


	public static void setFLAG_LOOP(boolean fLAG_LOOP) {
		FLAG_LOOP = fLAG_LOOP;
	}


	///// M�TODOS ////////////////////
	public static void gravaArquivo() throws IOException{
		arquivo = new FileWriter(myDocumentPath);
		grava_arq = new PrintWriter(arquivo);
	}
	
	
	public static void criaArqProp() throws IOException {				
		//cria o arquivo se ele nao existir
		if(!fProp.exists())	{
			fProp.createNewFile(); 
			gravaProp();
			}
		}
	
	//Grava arquivo properties
	public static void gravaProp() throws IOException {
		prop.setProperty("Str_n_page", Integer.toString(n_page));
		FileOutputStream Oprop = new FileOutputStream(pathProp); //instancia de gravacao
		prop.store(Oprop, null);
	}
	
	public static void carregaArquivoProp() throws IOException {
		FileInputStream Iprop = new FileInputStream(pathProp); //instancia de leitura
		prop.load(Iprop); //carrega o arquivo
		Str_n_page = prop.getProperty("Str_n_page"); 
		//n_page = Integer.parseInt("Str_n_page");  //pega o numero da pagina		
		Iprop.close();	
	}
		
	public static void inicioHtml() throws IOException {//Grava arquivo html
		gravaArquivo();
		grava_arq.println("<html> <head> </head> <body>");
		}
	
	public static void fimHtml() throws IOException {
		//gravaArquivo();
		grava_arq.println("<h1>FIM DE BOLETOS</h1>");
		grava_arq.println("</p>ULTIMO INDICE ACESSADO: " + n_page);
		grava_arq.println("<body> <html>");
		grava_arq.close();				
	}
	
//		//n_page = Integer.parseInt(JOptionPane.showInputDialog("�ltimo �ndice"));
//		String nome_busca = tela.gets_nome();
//		//String nome_busca = JOptionPane.showInputDialog("Digite o nome"); 
//		nome_busca = nome_busca.toUpperCase();
//		//System.out.print(nome_busca);

    	//Busca nomes - Entra com o nome a ser buscado e retorna um array com NOME, VALIDADE e N_page
	public static String[] getNomes(String nome_busca, String Str_n_page) throws IOException {		
		if(Str_n_page!=null && isFLAG_LOOP()) {  //se não vier nada no campo de pagina e eh a primeira execucao, pega o q estiver no arq properties
			carregaArquivoProp();  //puxa do arquivo já em inteiro
			setFLAG_LOOP(false);
		}
		n_page = Integer.parseInt(Str_n_page);
		Document doc = Jsoup.connect(raiz_url+n_page).get();
		doc.baseUri();
		Elements fim_page = doc.select("body > h2");

		if(fim_page.toString().indexOf("BOLETO")>0) {    //se achar a palavra boleto retorna FIM
			Ret_busca[0] = "FIM";
			Ret_busca[1] = "FIM";
		}
		else {
			//setFLAG_LOOP(true);
			n++;					
			Elements nome = doc.select("#boleto > table:nth-child(16) > tbody > tr.campos > td > p:nth-child(1)");
			Elements vencimento = doc.select("#boleto > table:nth-child(11) > tbody > tr.campos > td.vencimento2");
			s_nome = nome.html();		
			s_nome = s_nome.substring(s_nome.indexOf("-")+2, s_nome.length());
			s_nome = Integer.toString(n) + " - " + s_nome;
			System.out.print(nome.html()+" - ");				
			System.out.println(vencimento.html());	
			//n_page++;
			Ret_busca[0] = s_nome;
			Ret_busca[1] = vencimento.html();
			Ret_busca[2] = Integer.toString(n_page);
		}
		return Ret_busca; //retorna a busca (nome, vencimento)
	}

	//Grava consulta em HTML
	public static void consulta_to_Html() throws IOException{
		//gravaArquivo();
		String url = raiz_url + Integer.toString(n_page);
		grava_arq.printf("<a href=\"" + url +"\"><br />" + Ret_busca[0] + " - " + Ret_busca[1] + "</a>");
		//<a href="url">link text</a>
	}
		

	public static void abreHtml() throws IOException {
		//JOptionPane.showMessageDialog(null, "Concluido! Abra o arquivo.");
		Desktop.getDesktop().open(new File (myDocumentPath));
		//System.exit(0);
		//Runtime.getRuntime().load("f:\\CCN.html");
	}

}
