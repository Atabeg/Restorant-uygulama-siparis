import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
public class Calistir extends JFrame implements ActionListener,ListSelectionListener{
        
		JList<String>[] jlist;
        DefaultListModel<String>[] dlm;
        DefaultListModel<Integer>[] id;
        DefaultListModel<Double> fiyat;
        JTextField jtmiktar;
        JComboBox<String>[] jcb;
        String[] jlb_text= {"Masalar","Ürünler","Miktar","Fiyat","Tutar"};
        String[] btn_isim= {"Ekle","Sil","Güncelle","Adisyon","Göster"};
        Connection baglanti;
        public Calistir() {
		 this.setTitle("Cafe Otomasyon");
		 this.setSize(750, 750);
		 this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 this.setLayout(new FlowLayout());
		 
		 JPanel main_jpn=new JPanel();
		 main_jpn.setPreferredSize(new Dimension(700,700));
		 main_jpn.setLayout(new FlowLayout());

		dlm=new DefaultListModel[jlb_text.length];
		jlist=new JList[dlm.length];
		jcb=new JComboBox[2];//
		id=new DefaultListModel[3];
		fiyat=new DefaultListModel<Double>();
		for(int i=0;i<dlm.length;i++)
		{    
			JPanel jpn=new JPanel();
			jpn.setPreferredSize(new Dimension(150,650));
			JLabel jlb=new JLabel(jlb_text[i]);
			jlb.setPreferredSize(new Dimension(100,25));
			dlm[i]=new DefaultListModel<String>();
			jlist[i]=new JList<String>(dlm[i]);
			jlist[i].addListSelectionListener(this);
			JScrollPane  jscp=new JScrollPane(jlist[i]);
			jscp.setPreferredSize(new Dimension(100,350));
			JButton jbtn=new JButton(btn_isim[i]);
			jbtn.setPreferredSize(new Dimension(100,25));
			jbtn.addActionListener(this);
			jbtn.setActionCommand(btn_isim[i]);
			jpn.add(jlb);
			jpn.add(jscp);
			if(i<jcb.length)
			{   id[i]=new DefaultListModel<Integer>();
				jcb[i]=new JComboBox<String>();
				jcb[i].setPreferredSize(new Dimension(100,25));
				jpn.add(jcb[i]);
			}
			if(i==jcb.length)
			{    id[i]=new DefaultListModel<Integer>();
				jtmiktar=new JTextField();
				jtmiktar.setPreferredSize(new Dimension(100,25));
				jpn.add(jtmiktar);
			}
			if(i>jcb.length)
			{
				JPanel bosjpn=new JPanel();
				bosjpn.setPreferredSize(new Dimension(100,25));
				jpn.add(bosjpn);
			}
			jpn.add(jbtn);
			main_jpn.add(jpn);
		}
		 combodoldur();
		 listedoldur();
		 this.add(main_jpn);
		 this.setVisible(true);
	}
  public static void main(String[] arg) {
	  new Calistir();
  }
@Override
public void actionPerformed(ActionEvent arg0) {
	switch(arg0.getActionCommand()) {
	case "Ekle":ekle();break;
	case "Sil":sil();break;
	case "Güncelle":guncelle();break;
	case "Adisyon":adisyon();break;
	case "Göster":goster();break;
	}
	
}
public void ekle() {
	int secilen_masa=jcb[0].getSelectedIndex();
	int secilen_urun=jcb[1].getSelectedIndex();
	if(secilen_masa>-1&&secilen_urun>-1&&jtmiktar.getText().length()>0) {
	try {
		double miktar=Double.parseDouble(jtmiktar.getText());
		int masa_id=id[0].getElementAt(secilen_masa);
		int urun_id=id[1].getElementAt(secilen_urun);
	   	   if(baglanti.isClosed())
			  baglan();
		   PreparedStatement sorgu=baglanti.prepareStatement("insert into Adisyon(MasaID,UrunID,Miktar) values(?,?,?)");
		   sorgu.setInt(1, masa_id);
		   sorgu.setInt(2, urun_id);
		   sorgu.setDouble(3, miktar);
		   sorgu.executeUpdate();
		   baglanti.close();
		   jtmiktar.setText("");
		   jcb[0].setSelectedIndex(0);
		   jcb[1].setSelectedIndex(0);
		   listedoldur();
	}catch(Exception ex)
	{
		JOptionPane.showMessageDialog(this, ex.getMessage());
	}
	}
}
public void sil() {
	int secilen=jlist[0].getSelectedIndex();
	if(secilen>-1) {
 	try {
 		if(baglanti.isClosed())
 			baglan();
         PreparedStatement sorgu=baglanti.prepareStatement("delete from Adisyon where ID=?");
         sorgu.setInt(1, id[2].getElementAt(secilen));
         sorgu.executeUpdate();
         baglanti.close();
         listedoldur();
	}
	catch(Exception ex)
	{
		JOptionPane.showMessageDialog(this, ex.getMessage());
	}
}
}
public void guncelle() {
	int secilen=jlist[0].getSelectedIndex();
	int secilen_masa=jcb[0].getSelectedIndex();
	int secilen_urun=jcb[1].getSelectedIndex();
	if(secilen>-1&&secilen_masa>-1&&secilen_urun>-1) {
	
		try {
			double miktar=Double.parseDouble(jtmiktar.getText());
			
			if(baglanti.isClosed())
				baglan();
		    int masa_id=id[0].getElementAt(secilen_masa);
			int urun_id=id[1].getElementAt(secilen_urun);
			int adisyon_id=id[2].getElementAt(secilen);//liste adisyon idler
		    PreparedStatement sorgu=baglanti.prepareStatement("update Adisyon set MasaID=?,UrunID=?,Miktar=? where ID=?");
			sorgu.setInt(1, masa_id);
			sorgu.setInt(2, urun_id);
			sorgu.setDouble(3, miktar);
			sorgu.setInt(4, adisyon_id);
            sorgu.executeUpdate();
            baglanti.close();
            listedoldur();
            jtmiktar.setText("");
		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(this, ex.getMessage());
		}
	}	
}
public void adisyon() {
	 int secilen=jcb[0].getSelectedIndex();
		if(secilen>-1) {
		try {
			if(baglanti.isClosed())
				baglan();
	       PreparedStatement sorgu=baglanti.prepareStatement("delete from Adisyon where MasaID=?");
		   sorgu.setInt(1, id[0].getElementAt(secilen));
		   sorgu.executeUpdate();
		   baglanti.close();
		  listedoldur();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	    
		}
    }
public void goster() {
	 int secilen=jcb[0].getSelectedIndex();
		if(secilen>-1) {
		try {
			if(baglanti.isClosed())
				baglan();
	       PreparedStatement sorgu=baglanti.prepareStatement("select u.UrunAd as 'UrunAd',(u.UrunFiyat*a.Miktar) as 'Tutar' from Adisyon a inner join Urunler u  on u.ID=a.UrunID where a.MasaID=?");
		   sorgu.setInt(1, id[0].getElementAt(secilen));
		   int sayac=0;
		   ResultSet veri=sorgu.executeQuery();
		  String urunler="";
		  double tutar=0.0;
		   while(veri.next())
		   {
			   if(urunler.length()>0)
				   urunler+=",";
			   urunler+=veri.getString("UrunAd");
			   tutar+=veri.getDouble("Tutar");
		   sayac++;
		   }
		   veri.close();
		   baglanti.close();
		   JOptionPane.showMessageDialog(this,"Ürün Sayısı="+sayac+" "+urunler+" Toplam Tutar="+tutar);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage());	
			}
		}
}
public void baglan(){
 
	try {
	baglanti=DriverManager.getConnection("jdbc:sqlite:otomasyon.db");
} catch (Exception e) {
	JOptionPane.showMessageDialog(this, e.getMessage());
}	
}
public void combodoldur(){
	PreparedStatement sorgu=null;
	ResultSet veri=null;
try {
	baglan();
	sorgu=baglanti.prepareStatement("select ID,UrunAd,UrunFiyat from Urunler");
	veri=sorgu.executeQuery();
    for(int i=0;i<jcb.length;i++)
    {
    	jcb[i].removeAllItems();
    	id[i].clear();
    }
     fiyat=new DefaultListModel<Double>();
	while(veri.next())
	{
	     jcb[1].addItem(veri.getString("UrunAd"));	
	     id[1].addElement(veri.getInt("ID"));
	     fiyat.addElement(veri.getDouble("UrunFiyat"));
	}
     veri.close();
     sorgu=baglanti.prepareStatement("select ID,MasaAdi from Masalar");
     veri=sorgu.executeQuery();
     while(veri.next())
     {
    	 jcb[0].addItem(veri.getString("MasaAdi"));
    	 id[0].addElement(veri.getInt("ID"));
     }
} catch (Exception e) {
	JOptionPane.showMessageDialog(this, e.getMessage());
}
}
public void listedoldur() {
	try {
		baglan();
		String sql="select a.ID as 'ID',m.MasaAdi as 'MasaAdi',u.UrunAd as 'UrunAd',a.Miktar as 'Miktar',u.UrunFiyat as 'Fiyat',u.UrunFiyat*a.Miktar as 'Tutar'  from Masalar m inner join Adisyon  a on m.ID=a.MasaID inner join Urunler u on u.ID=a.UrunID"; 
		PreparedStatement sorgu=baglanti.prepareStatement(sql);
		for(int i=0;i<dlm.length;i++)
			dlm[i].clear();
		id[2].clear();
		ResultSet veri=sorgu.executeQuery();
		while(veri.next())
		{   id[2].addElement(veri.getInt("ID"));
			dlm[0].addElement(veri.getString("MasaAdi"));
			dlm[1].addElement(veri.getString("UrunAd"));
			double miktar=veri.getDouble("Miktar");
			dlm[2].addElement(miktar+"");
			double fiyat=veri.getDouble("Fiyat");
			dlm[3].addElement(fiyat+"");	
			double tutar=fiyat*miktar;
			dlm[4].addElement(""+tutar);
		}
		veri.close();
		baglanti.close();
	} catch (Exception e) {
		JOptionPane.showMessageDialog(this, e.getMessage());
	}
	
}
@Override
public void valueChanged(ListSelectionEvent e) {
	JList secilen=(JList)e.getSource();
	for(int i=0;i<jlist.length;i++)
		 jlist[i].setSelectedIndex(secilen.getSelectedIndex());
	
}
}