package com.bbs.iTextpdf;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.os.Environment;

import com.bbs.listeners.BBSCreateDocumentListener;
import com.bbs.model.BBSCharge;
import com.bbs.model.BBSDonation;
import com.bbs.model.BBSMileage;
import com.bbs.model.BBSReceipt;
import com.bbs.taxapp.R;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class BBSCreatePdf {
	
	 private static String PDF_ROUTE 		= "/BBSData/BBSPdf/";
	 private static String PDF_DONATION 	= "BBSDonations.pdf";
	 private static String PDF_MILEAGE 		= "BBSMileage.pdf";
	 private static String PDF_CHARGE 		= "BBSCharges.pdf";
	 private static String PDF_RECEIPT 		= "BBSReceipts.pdf";
	 private static String PDF_DOCUMENT 	= "BBSDocument.pdf";
 
	 private static Font catFont = null;
	 private static Font redFont = null;
	 private static Font subFont = null;
	 private static Font smallBold = null;
	 
	  static BBSCreatePdf g_instance;
	  private static Context mContext;
	  private static BBSCreateDocumentListener mListener;
	  
	  private static ArrayList<BBSDonation> mArrayDonations;
	  private static ArrayList<BBSMileage> mArrayMileage;
	  private static ArrayList<BBSCharge> mArrayCharges;
	  private static ArrayList<BBSReceipt> mArrayReceipts;
	  private static String docName;
	  private static String docImageUrl;
	  
	  private static String routeUrl;
	  
	  public BBSCreatePdf(Context ctx, BBSCreateDocumentListener listener) {
		  mContext = ctx;
		  mListener = listener;
		  routeUrl = Environment.getExternalStorageDirectory() + PDF_ROUTE;
		  
		  File newFolder = new File(routeUrl);
		  if (!newFolder.exists()) {
			  newFolder.mkdirs();
		  }
		  
		  catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
		  redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.RED);
		  subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
		  smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
	  }
	  
	  public static BBSCreatePdf getInstance(Context ctx, BBSCreateDocumentListener listener) {
		  if (g_instance == null)
				g_instance = new BBSCreatePdf(ctx, listener);
			
			return g_instance;
	  }
	  
	  private boolean createFile(String file_url) {
		  try {
			  File newFile = new File(file_url);
		    	
			  if (!newFile.exists()) {
				  newFile.createNewFile();
			  }  
			  return true;
		  } catch (IOException e) {
			  e.printStackTrace();
			  return false;
		  }
	  }

	  public void createDonationsPage(ArrayList<BBSDonation> donations) {
	    try {
	    	
	    	String pdfDonationUrl = routeUrl + PDF_DONATION;
	    	createFile(pdfDonationUrl);
	    	
	    	mArrayDonations = donations;
	      Document document = new Document();
	      PdfWriter.getInstance(document, new FileOutputStream(pdfDonationUrl));
	      document.open();
	      addMetaData(document);
	      addDonationPage(document);
	      document.close();
	      mListener.onDocumentCreated(pdfDonationUrl);
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  }
	  
	  public void createMileagePage(ArrayList<BBSMileage> mileage) {
	    try {
	    	String pdfMileageUrl = routeUrl + PDF_MILEAGE;
	    	createFile(pdfMileageUrl);
	    	
	    	mArrayMileage = mileage;
	      Document document = new Document();
	      PdfWriter.getInstance(document, new FileOutputStream(pdfMileageUrl));
	      document.open();
	      addMetaData(document);
	      addMileagePage(document);
	      document.close();
	      mListener.onDocumentCreated(pdfMileageUrl);
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  }
	  
	  public void createChargePage(ArrayList<BBSCharge> charges) {
	    try {
	    	String pdfChargeUrl = routeUrl + PDF_CHARGE;
	    	createFile(pdfChargeUrl);
	    	
	    	mArrayCharges = charges;
	      Document document = new Document();
	      PdfWriter.getInstance(document, new FileOutputStream(pdfChargeUrl));
	      document.open();
	      addMetaData(document);
	      addChargesPage(document);
	      document.close();
	      mListener.onDocumentCreated(pdfChargeUrl);
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  }
	  
	  public void createReceiptsPage(ArrayList<BBSReceipt> receipts) {
	    try {
	    	String pdfReceiptUrl = routeUrl + PDF_RECEIPT;
	    	createFile(pdfReceiptUrl);
	    	
	    	mArrayReceipts = receipts;
	      Document document = new Document();
	      PdfWriter.getInstance(document, new FileOutputStream(pdfReceiptUrl));
	      document.open();
	      addMetaData(document);
	      addReceiptsPage(document);
	      document.close();
	      mListener.onDocumentCreated(pdfReceiptUrl);
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  }
	  
	  public void createDocumentPage(String doc_name, String doc_imgUrl) {
	    try {
	    	String pdfDocumentUrl = routeUrl + PDF_DOCUMENT;
	    	createFile(pdfDocumentUrl);
	    	
	    	docName = doc_name;
	    	docImageUrl = doc_imgUrl;
	      Document document = new Document();
	      PdfWriter.getInstance(document, new FileOutputStream(pdfDocumentUrl));
	      document.open();
	      addMetaData(document);
	      addDocumentPage(document);
	      document.close();
	      mListener.onDocumentCreated(pdfDocumentUrl);
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  }

	  // iText allows to add metadata to the PDF which can be viewed in your Adobe
	  // Reader
	  // under File -> Properties
	  private static void addMetaData(Document document) {
	    document.addTitle("BBS PDF");
	    document.addSubject("Using iText");
	    document.addKeywords("Java, PDF, iText");
	    document.addAuthor("BBS TaxApp");
	    document.addCreator("BBS TaxApp");
	  }

	  private static void addDonationPage(Document document)
		  throws DocumentException {
		  	Paragraph preface = new Paragraph();
			// We add one empty line
			addEmptyLine(preface, 1);
			// Lets write a big header
			preface.add(new Paragraph(mContext.getResources().getString(R.string.donation_history), catFont));
			
			addEmptyLine(preface, 1);
			
			document.add(preface);
			
			PdfPTable table = createTable(0);
			document.add(table);
			// Start a new page
			document.newPage();
	  }
	  
	  private static void addMileagePage(Document document)
		      throws DocumentException {
		  Paragraph preface = new Paragraph();
			// We add one empty line
			addEmptyLine(preface, 1);
			// Lets write a big header
			preface.add(new Paragraph(mContext.getResources().getString(R.string.mileage_history), catFont));
			
			addEmptyLine(preface, 1);
			
			document.add(preface);
			
			PdfPTable table = createTable(1);
			document.add(table);
			// Start a new page
			document.newPage();
	  }
	  
	  private static void addChargesPage(Document document)
		      throws DocumentException {
		  Paragraph preface = new Paragraph();
			// We add one empty line
			addEmptyLine(preface, 1);
			// Lets write a big header
			preface.add(new Paragraph(mContext.getResources().getString(R.string.charges_history), catFont));
			
			addEmptyLine(preface, 1);
			
			document.add(preface);
			
			PdfPTable table = createTable(2);
			document.add(table);
			// Start a new page
			document.newPage();
	  }
	  
	  private static void addReceiptsPage(Document document)
		      throws DocumentException {
		
		// Second parameter is the number of the chapter
		  Paragraph preface = new Paragraph();
		// We add one empty line
		addEmptyLine(preface, 1);
		// Lets write a big header
		preface.add(new Paragraph(mContext.getResources().getString(R.string.receipts_history), catFont));
		
		addEmptyLine(preface, 1);
		
		document.add(preface);
		try {
			for (int i = 0; i < mArrayReceipts.size(); i++) {
				
				BBSReceipt receipt = mArrayReceipts.get(i);
				
				Paragraph rep_name = new Paragraph(receipt.getReceiptName(), subFont);
				Paragraph rep_category = new Paragraph("Category: " + receipt.getReceiptCategory(), smallBold);
				Paragraph rep_date = new Paragraph("Date: " + receipt.getReceiptDate(), smallBold);
				Paragraph rep_amount = new Paragraph("Amount: $" + receipt.getReceiptAmount(), smallBold);
				
				document.add(rep_name);
				document.add(rep_category);
				document.add(rep_date);
				document.add(rep_amount);
				
				String img_url = receipt.getReceiptImage();
				Image image = ITextImageFromUrl(document, img_url);
				document.add(image);
				document.newPage();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	  }
	  
	  private static void addDocumentPage(Document document)
		      throws DocumentException {
		// Second parameter is the number of the chapter
		  Paragraph preface = new Paragraph();
		// We add one empty line
		addEmptyLine(preface, 1);
		// Lets write a big header
		preface.add(new Paragraph(mContext.getResources().getString(R.string.document_name) + ": " +  docName, catFont));
		addEmptyLine(preface, 1);
		
		document.add(preface);
		
		Image image = ITextImageFromUrl(document, docImageUrl);
		document.add(image);
		
	  }

	  private static PdfPTable createTable(int tableType)
	      throws BadElementException {
	    PdfPTable table = new PdfPTable(3);

	    // t.setBorderColor(BaseColor.GRAY);
	    // t.setPadding(4);
	    // t.setSpacing(4);
	    // t.setBorderWidth(1);
	    
	    if (tableType == 0) {
	    	table = new PdfPTable(3);
	    	
	    	PdfPCell c1 = new PdfPCell(new Phrase(mContext.getResources().getString(R.string.donation_name)));
		    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		    table.addCell(c1);

		    c1 = new PdfPCell(new Phrase(mContext.getResources().getString(R.string.date)));
		    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		    table.addCell(c1);

		    c1 = new PdfPCell(new Phrase(mContext.getResources().getString(R.string.donation_amount)));
		    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		    table.addCell(c1);
		    table.setHeaderRows(1);
		    
		    for( int i = 0; i < mArrayDonations.size(); i++) {
		    	BBSDonation don = mArrayDonations.get(i);
		    	table.addCell(don.getDonationName());
		    	table.addCell(don.getDonationDate());
		    	table.addCell("$" + don.getDonationAmount());
		    }
	    } else if (tableType == 1) {
	    	table = new PdfPTable(7);
	    	
	    	PdfPCell c1 = new PdfPCell(new Phrase(mContext.getResources().getString(R.string.reason)));
		    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		    table.addCell(c1);

		    c1 = new PdfPCell(new Phrase(mContext.getResources().getString(R.string.date)));
		    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		    table.addCell(c1);

		    c1 = new PdfPCell(new Phrase(mContext.getResources().getString(R.string.mileage_bamount)));
		    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		    table.addCell(c1);
		    
		    c1 = new PdfPCell(new Phrase(mContext.getResources().getString(R.string.mileage_eamount)));
		    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		    table.addCell(c1);
		    
		    c1 = new PdfPCell(new Phrase(mContext.getResources().getString(R.string.car_make)));
		    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		    table.addCell(c1);
		    
		    c1 = new PdfPCell(new Phrase(mContext.getResources().getString(R.string.car_model)));
		    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		    table.addCell(c1);
		    
		    c1 = new PdfPCell(new Phrase(mContext.getResources().getString(R.string.car_year)));
		    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		    table.addCell(c1);
		    table.setHeaderRows(1);
		    
		    for( int i = 0; i < mArrayMileage.size(); i++) {
		    	BBSMileage mileage = mArrayMileage.get(i);
		    	table.addCell(mileage.getMileageReason());
		    	table.addCell(mileage.getMileageDate());
		    	table.addCell(Integer.toString(mileage.getMileageBAmount()));
		    	table.addCell(Integer.toString(mileage.getMileageEAmount()));
		    	table.addCell(mileage.getMileageCarMake());
		    	table.addCell(mileage.getMileageCarModel());
		    	table.addCell(mileage.getMileageCarYear());
		    }	    	
	    } else if (tableType == 2) {
	    	table = new PdfPTable(3);
	    	
	    	PdfPCell c1 = new PdfPCell(new Phrase(mContext.getResources().getString(R.string.charge_name)));
		    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		    table.addCell(c1);

		    c1 = new PdfPCell(new Phrase(mContext.getResources().getString(R.string.date)));
		    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		    table.addCell(c1);

		    c1 = new PdfPCell(new Phrase(mContext.getResources().getString(R.string.charge_amount)));
		    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		    table.addCell(c1);
		    table.setHeaderRows(1);
		    
		    for( int i = 0; i < mArrayCharges.size(); i++) {
		    	BBSCharge charge = mArrayCharges.get(i);
		    	table.addCell(charge.getChargeName());
		    	table.addCell(charge.getChargeDate());
		    	table.addCell("$" + charge.getChargeAmount());
		    }
	    }
	    
	    return table;
	  }

	  private static void addEmptyLine(Paragraph paragraph, int number) {
	    for (int i = 0; i < number; i++) {
	      paragraph.add(new Paragraph(" "));
	    }
	  }
	  
	  private static Image ITextImageFromUrl(Document document, String img_url) {
		  try {
			int indentation = 0;
			
//			Image image = Image.getInstance(new URL("file://" + img_url));
//			Bitmap bmp = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), (new URL()));
			
			Bitmap bmp = BitmapFactory.decodeFile(img_url);
			float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
		               - document.rightMargin() - indentation) / bmp.getWidth()) * 100;
			Matrix m = new Matrix();
			m.setRectToRect(new RectF(0, 0, bmp.getWidth(), bmp.getHeight()), new RectF(0, 0, bmp.getWidth() * scaler / 100, bmp.getHeight() * scaler / 100), Matrix.ScaleToFit.CENTER);
			
			Bitmap scaled_bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), m, true);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			scaled_bmp.compress(Bitmap.CompressFormat.JPEG, 80, stream);
			
			Image image = Image.getInstance(stream.toByteArray());
			
			ExifInterface exif = new ExifInterface(img_url);
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
			if (orientation == 6) {
				image.setRotationDegrees(-90f);
			}

//			image.scalePercent(scaler);
			
			return image;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	  }
}