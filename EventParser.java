import java.io.*;
import java.sql.*;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.Scanner;


public class EventParser {
	static LocalDateTime currentTime = LocalDateTime.now();
	static String dateNow = currentTime.getDayOfMonth()+"_"+currentTime.getMonth()+"_"+"_"+currentTime.getYear()+currentTime.getNano();
	public static void main(String[] args) throws IOException, XMLStreamException, SQLException, InterruptedException {
		long ProgramStartTime = System.currentTimeMillis();

		String dbURL1 = "jdbc:mysql://localhost:3306/"+ args[1].trim();
		String dbURL2 = "jdbc:sqlserver://localhost;databaseName=city;encrypt=true;trustServerCertificate=true;sslProtocol=TLSv1.2;";
		Properties properties = new Properties();
		properties.put("user", args[2]);
		properties.put("password", args[3]);
		//properties.put("database", "BB");
		Connection conn = DriverManager.getConnection(dbURL1, properties);
		Statement st  = conn.createStatement();
		//Create_SP_Clean(conn);
		String createTable = CreateTable2("par_normal_temp");
		st.execute(createTable);
		createTable = CreateTable2("par_rec_dispute");
		st.execute(createTable);
		createTable = CreateTable2("par_mtemp");
		st.execute(createTable);
		createTable = CreateTable2("par_normal_ext");
		st.execute(createTable);
		createTable = CreateTable2("par_orphan_rev");
		st.execute(createTable);
		createTable = CreateTable2("par_dump");
		st.execute(createTable);
		Create_SP_Report(conn);
		Create_SP_Clean(conn);
		st.close();


		File directoryPath = new File(args[0]);
		//List of all files and directories
		File filesList[] = directoryPath.listFiles();
		for(File fileContent : filesList) {
			String fileName = fileContent.getName();

			if(fileName.contains(".xml")){
				File file = new File(fileName+"_"+dateNow+".txt");
				File path = new File(directoryPath+"\\"+fileName);
				System.out.println();
				System.out.println("File Name="+fileName);
				FileHeader fileHeader = new FileHeader();
				FileTrailer fileTrailer = new FileTrailer();
				XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
				XMLEventReader reader = xmlInputFactory.createXMLEventReader(new FileInputStream(path));
				//ArrayList<DocClass> docList = new ArrayList<DocClass>();
				DocClass publicDoc = new DocClass();
				int i=0;
				System.out.println();
				while(reader.hasNext()){
					XMLEvent nextEvent = reader.nextEvent();
					if (nextEvent.isStartElement()) {
						StartElement startElement = nextEvent.asStartElement();
						if(startElement.getName().getLocalPart().equals("Doc")){
							DocClass doc = new DocClass();
							publicDoc = doc;
							i++;
							while(reader.hasNext()){
								nextEvent = reader.nextEvent();
								if (nextEvent.isStartElement()) {
									startElement = nextEvent.asStartElement();
									if(startElement.getName().getLocalPart().equals("TransType")){
										publicDoc = ReadTransType(publicDoc,reader);
									}else if(startElement.getName().getLocalPart().equals("DocRefSet")){
										publicDoc = ReadDocRefSet(publicDoc,reader);
									}else if(startElement.getName().getLocalPart().equals("LocalDt")){
										publicDoc.setLocalDt(reader.nextEvent().toString());
									}else if(startElement.getName().getLocalPart().equals("NWDt")){
										publicDoc.setNWDt(reader.nextEvent().toString());
									}else if(startElement.getName().getLocalPart().equals("SourceDtls")){
										publicDoc = ReadSourceDtls(publicDoc,reader);
									}else if(startElement.getName().getLocalPart().equals("Originator")){
										publicDoc = ReadOriginator(publicDoc,reader);
									}else if(startElement.getName().getLocalPart().equals("Destination")){
										publicDoc = ReadDestination(publicDoc,reader);
									}else if(startElement.getName().getLocalPart().equals("Transaction")){
										publicDoc = ReadTransaction(publicDoc,reader);
									}else if(startElement.getName().getLocalPart().equals("Billing")){
										publicDoc = ReadBilling(publicDoc,reader);
									}else if(startElement.getName().getLocalPart().equals("Reconciliation")){
										publicDoc = ReadReconciliation(publicDoc,reader);
									}else if(startElement.getName().getLocalPart().equals("Status")){
										publicDoc = ReadStatus(publicDoc,reader);
									}
								}else if (nextEvent.isEndElement() && nextEvent.asEndElement().getName().toString().equals("Doc")) {
									FileWriter fr = new FileWriter(file, true);
									BufferedWriter bw = new BufferedWriter(fr);
									String sql;
									sql = "insert into par_normal_temp(Sender,CreationDate,CreationTime,FileSeqNumber,Receiver,MsgCode,ServiceClass,DRN,SRN,RRN,ARN,AuthCode,LocalDt,NWDt,SIC,Country,City,MerchantName,MerchantID,OrgContractNumber,OrgMemberId,OrgTransitId,OrgChannel,DestContractNumber,DestContractNumberMask,DestMemberId,DestTransitId,DestRelation,DestChannel,TranCurrency,TranAmount,WhatTrx,CpId,BillPhaseDate,BillCurrency,BillAmount,ReconPhaseDate,ReconCurrency,ReconAmount,RecsCount,HashTotalAmount)";
									sql += "values('" + fileHeader.getSender() + "','" + fileHeader.getCreationDate() + "','" + fileHeader.getCreationTime() + "','" + fileHeader.getFileSeqNumber() + "','" + fileHeader.getReceiver() + "','" + publicDoc.getMsgCode() + "','" + publicDoc.getServiceClass() + "'";
									sql += ",'" + publicDoc.getDRN() + "','" + publicDoc.getSRN() + "','" + publicDoc.getRRN() + "','" + publicDoc.getARN() + "','" + publicDoc.getAuthCode() + "','" + publicDoc.getLocalDt() + "','" + publicDoc.getNWDt() + "','" + publicDoc.getSourceDetails().getSIC() + "','" +  publicDoc.getSourceDetails().getCountry() + "','" +  publicDoc.getSourceDetails().getCity() + "','" +  publicDoc.getSourceDetails().getMerchantName() + "', '" +  publicDoc.getSourceDetails().getMerchantID() + "','" +  publicDoc.getOriginator().getContractNumber() + "','" +publicDoc.getOriginator().getMemberId()  + "','" + ""+ "','" + publicDoc.getOriginator().getChannel()+ "','" + publicDoc.getDestination().getContractNumber() + "','";
									if(publicDoc.getDestination().getContractNumber().length()>12){ sql+=getMasked(publicDoc.getDestination().getContractNumber());}
									else{sql+=publicDoc.getDestination().getContractNumber();};
									sql+="','" + publicDoc.getDestination().getMemberId() + "','" + ""+ "','" + publicDoc.getDestination().getRelation() + "','" + publicDoc.getDestination().getChannel()+ "'";
									sql += ",'" + publicDoc.getTransaction().getCurrency() + "','" + publicDoc.getTransaction().getAmount() + "','" + publicDoc.getTransaction().getWHAT_TRX() + "','" + publicDoc.getTransaction().getCPID() + "','" +publicDoc.getBilling().getPhaseDate()+ "','" + publicDoc.getBilling().getCurrency()+ "','" + publicDoc.getBilling().getAmount()+ "','" + publicDoc.getReconciliation().getPhaseDate() + "','" + publicDoc.getReconciliation().getCurrency() + "','" + publicDoc.getReconciliation().getAmount() + "','";
									sql += "\n";
									//System.out.println(sql);
									bw.write(sql);
									bw.close();
									fr.close();
									System.out.flush();
									System.out.print("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b");
									System.out.print("Reading Doc "+i);
									//Thread.sleep(1);
									break;
								}
							}
							//System.out.println(publicDoc.getDestination().getContractNumber());
						}else if(startElement.getName().getLocalPart().equals("FileHeader")){
							fileHeader = ReadFileHeader(reader);
						}
						else if(startElement.getName().getLocalPart().equals("FileTrailer")){

							fileTrailer=ReadFileTrailer(reader);
						}
					}
				}
				if(i==Integer.parseInt(fileTrailer.getRecsCount())){
					System.out.println();
					System.out.println("File count matched");
					String line;
					try {
						int l=0;
						Scanner scan = new Scanner(new FileReader(fileName+"_"+dateNow+".txt"));
						Statement statement = conn.createStatement();
						while(scan.hasNextLine()){
							line = scan.nextLine().trim();
							line +=fileTrailer.getRecsCount() + "','" + fileTrailer.getHashTotalAmount() + "')";
							statement.executeUpdate(line);
							System.out.flush();
							System.out.print("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b");
							System.out.print("Inserting doc "+ ++l);
							//	Thread.sleep(1);
						}
						scan.close();
						file.renameTo(new File(fileName + dateNow + "_Done.txt"));
						MakeSqlOperation(conn);
					}
					catch(IOException e){
						System.out.println(e);
					}
				}else{
					System.out.println("File count not matched");
				}
			}
		}
		//System.out.println(i + " "+ k);
		long ProgramEndTime   = System.currentTimeMillis();
		double totalTime = (ProgramEndTime - ProgramStartTime)/1000d;
		System.out.println();
		System.out.println("Total Execution time >> "+totalTime + " seconds.");
}
	private static FileHeader ReadFileHeader(XMLEventReader reader) throws XMLStreamException{
		FileHeader fileHeader= new FileHeader();
		while(reader.hasNext()){
			XMLEvent nextEvent = reader.nextEvent();
			if (nextEvent.isStartElement()) {
				StartElement startElement = nextEvent.asStartElement();
				if(startElement.getName().getLocalPart().equals("FileLabel")){
					fileHeader.setFileLabel(reader.nextEvent().toString());
				}else if(startElement.getName().getLocalPart().equals("FormatVersion")){
					fileHeader.setFormatVersion(reader.nextEvent().toString());
				}else if(startElement.getName().getLocalPart().equals("Sender")){
					fileHeader.setSender(reader.nextEvent().toString());
				}else if(startElement.getName().getLocalPart().equals("CreationDate")){
					fileHeader.setCreationDate(reader.nextEvent().toString());
				}else if(startElement.getName().getLocalPart().equals("CreationTime")){
					fileHeader.setCreationTime(reader.nextEvent().toString());
				}else if(startElement.getName().getLocalPart().equals("FileSeqNumber")){
					fileHeader.setFileSeqNumber(reader.nextEvent().toString());
				}else if(startElement.getName().getLocalPart().equals("Receiver")){
					fileHeader.setReceiver(reader.nextEvent().toString());
				}
			}
			if (nextEvent.isEndElement() && nextEvent.asEndElement().getName().toString().equals("FileHeader")) {

				return fileHeader;
			}
		}
		return fileHeader;
	}
	private static  FileTrailer ReadFileTrailer(XMLEventReader reader) throws XMLStreamException{
		FileTrailer fileTrailer= new FileTrailer();
		while(reader.hasNext()){
			XMLEvent nextEvent = reader.nextEvent();
			if (nextEvent.isStartElement()) {
				StartElement startElement = nextEvent.asStartElement();
				if(startElement.getName().getLocalPart().equals("RecsCount")){
					fileTrailer.setRecsCount(reader.nextEvent().toString());
				}else if(startElement.getName().getLocalPart().equals("HashTotalAmount")){
					fileTrailer.setHashTotalAmount(reader.nextEvent().toString());
				}
			}
			if (nextEvent.isEndElement() && nextEvent.asEndElement().getName().toString().equals("FileTrailer")) {
				//publicDoc.setFileTrailer(fileTrailer);
				return fileTrailer;
			}
		}
		return fileTrailer;
	}
	private static  DocClass ReadTransType(DocClass publicDoc,XMLEventReader reader) throws XMLStreamException{
	 //SkippReader(reader,4);
	while(reader.hasNext()){
		XMLEvent nextEvent = reader.nextEvent();
		if (nextEvent.isStartElement()) {
	        StartElement startElement = nextEvent.asStartElement();
	        if(startElement.getName().getLocalPart().equals("RequestCategory")){
	        	 publicDoc.setRequestCategory(reader.nextEvent().toString());
	        }else if(startElement.getName().getLocalPart().equals("ServiceClass")){
	        	publicDoc.setServiceClass(reader.nextEvent().toString());
	        }else if(startElement.getName().getLocalPart().equals("TransTypeCode")){
	        	publicDoc.setTransTypeCode(reader.nextEvent().toString());
	        }
	     }
		 if (nextEvent.isEndElement() && nextEvent.asEndElement().getName().toString().equals("TransCode")) {
			 return publicDoc;
		 }
	}
	return publicDoc;
 }

	private static  DocClass ReadDocRefSet(DocClass publicDoc,XMLEventReader reader) throws XMLStreamException{
	 while(reader.hasNext()){
			XMLEvent nextEvent = reader.nextEvent();
			if (nextEvent.isStartElement()) {
		        StartElement startElement = nextEvent.asStartElement();
		        if(startElement.getName().getLocalPart().equals("ParmCode")){
		        	String paramCode = reader.nextEvent().toString();
		        	if(paramCode.equals("DRN")){
						SkippReader(reader,2);
						XMLEvent nextVal = reader.nextEvent();
						if (nextVal.isStartElement()){
							startElement = nextVal.asStartElement();
							if(startElement.getName().getLocalPart().equals("Value")){
								publicDoc.setDRN(reader.nextEvent().toString());
							}
						}
					}else if(paramCode.equals("PrevDRN")){
						SkippReader(reader,2);
						XMLEvent nextVal = reader.nextEvent();
						if (nextVal.isStartElement()){
							startElement = nextVal.asStartElement();
							if(startElement.getName().getLocalPart().equals("Value")){
								publicDoc.setPrevDRN(reader.nextEvent().toString());
							}
						}
					}else if(paramCode.equals("OrigDRN")){
						SkippReader(reader,2);
						XMLEvent nextVal = reader.nextEvent();
						if (nextVal.isStartElement()){
							startElement = nextVal.asStartElement();
							if(startElement.getName().getLocalPart().equals("Value")){
								publicDoc.setOrigDRN(reader.nextEvent().toString());
							}
						}
					}else if (paramCode.equals("SRN")){
						SkippReader(reader,2);
						XMLEvent nextVal = reader.nextEvent();
						if (nextVal.isStartElement()){
							startElement = nextVal.asStartElement();
							if(startElement.getName().getLocalPart().equals("Value")){
		        				publicDoc.setSRN(reader.nextEvent().toString());
							}
						}
		        	}else if(paramCode.equals("RRN")){
						SkippReader(reader,2);
						XMLEvent nextVal = reader.nextEvent();
						if (nextVal.isStartElement()){
							startElement = nextVal.asStartElement();
							if(startElement.getName().getLocalPart().equals("Value")){
		        				publicDoc.setRRN(reader.nextEvent().toString());
							}
						}
		        	}
		        }
		     }
			 if (nextEvent.isEndElement() && nextEvent.asEndElement().getName().toString().equals("DocRefSet")) {
				 return publicDoc;
			 }
		}
		return publicDoc; 
 }


	private static  DocClass ReadSourceDtls(DocClass publicDoc,XMLEventReader reader) throws XMLStreamException{
		SourceDetails details = new SourceDetails();
		while(reader.hasNext()){
			XMLEvent nextEvent = reader.nextEvent();
			if (nextEvent.isStartElement()) {
				StartElement startElement = nextEvent.asStartElement();
				if(startElement.getName().getLocalPart().equals("SIC")){
					details.setSIC(reader.nextEvent().toString());
					//publicDoc.setSIC();
				}else if(startElement.getName().getLocalPart().equals("Country")){
					details.setCountry(reader.nextEvent().toString());
				}else if(startElement.getName().getLocalPart().equals("MerchantName")){
					details.setMerchantName(reader.nextEvent().toString());
				}else if(startElement.getName().getLocalPart().equals("MerchantID")){
					details.setMerchantID(reader.nextEvent().toString());
				}
			}
			if (nextEvent.isEndElement() && nextEvent.asEndElement().getName().toString().equals("SourceDtls")) {
				publicDoc.setSourceDetails(details);
				return publicDoc;
			}
		}
		return publicDoc;
	}
	private static  DocClass ReadOriginator(DocClass publicDoc,XMLEventReader reader) throws XMLStreamException{
		Originator originator = new Originator();
		while(reader.hasNext()){
			XMLEvent nextEvent = reader.nextEvent();
			if (nextEvent.isStartElement()) {
				StartElement startElement = nextEvent.asStartElement();
				if(startElement.getName().getLocalPart().equals("ContractNumber")){
					originator.setContractNumber(reader.nextEvent().toString());
				}else if(startElement.getName().getLocalPart().equals("Institution")){
					originator.setInstitution(reader.nextEvent().toString());
				}else if(startElement.getName().getLocalPart().equals("MemberId")){
					originator.setMemberId(reader.nextEvent().toString());
				}else if(startElement.getName().getLocalPart().equals("TransitContractNumber")){
					originator.setContractNumber(reader.nextEvent().toString());
				}else if(startElement.getName().getLocalPart().equals("Channel")){
					originator.setChannel(reader.nextEvent().toString());
				}
			}
			if (nextEvent.isEndElement() && nextEvent.asEndElement().getName().toString().equals("Originator")) {
				publicDoc.setOriginator(originator);
				return publicDoc;
			}
		}
		return publicDoc;
	}
	private static  DocClass ReadDestination(DocClass publicDoc,XMLEventReader reader) throws XMLStreamException{
		Destination destination = new Destination();
		while(reader.hasNext()){
			XMLEvent nextEvent = reader.nextEvent();
			if (nextEvent.isStartElement()) {
				StartElement startElement = nextEvent.asStartElement();
				if(startElement.getName().getLocalPart().equals("ContractNumber")){
					destination.setContractNumber(reader.nextEvent().toString());
				}else if(startElement.getName().getLocalPart().equals("Institution")){
					destination.setInstitution(reader.nextEvent().toString());
				}else if(startElement.getName().getLocalPart().equals("MemberId")){
					destination.setMemberId(reader.nextEvent().toString());
				}else if(startElement.getName().getLocalPart().equals("TransitContractNumber")){
					destination.setTransitContractNumber(reader.nextEvent().toString());
				}else if(startElement.getName().getLocalPart().equals("Channel")){
					destination.setChannel(reader.nextEvent().toString());
				}else if(startElement.getName().getLocalPart().equals("Relation")){
					destination.setRelation(reader.nextEvent().toString());
				}
			}
			if (nextEvent.isEndElement() && nextEvent.asEndElement().getName().toString().equals("Destination")) {
				publicDoc.setDestination(destination);
				return publicDoc;
			}
		}
		return publicDoc;
	}

	private static  DocClass ReadTransaction(DocClass publicDoc,XMLEventReader reader) throws XMLStreamException{
		Transaction transaction = new Transaction();
		while(reader.hasNext()){
			XMLEvent nextEvent = reader.nextEvent();
			if (nextEvent.isStartElement()) {
				StartElement startElement = nextEvent.asStartElement();
				if(startElement.getName().getLocalPart().equals("Currency")){
					transaction.setCurrency(reader.nextEvent().toString());
				}else if(startElement.getName().getLocalPart().equals("Amount")){
					transaction.setAmount(reader.nextEvent().toString());
				}else if(startElement.getName().getLocalPart().equals("ParmCode")){
					String paramCode = reader.nextEvent().toString();
					if(paramCode.equals("AUTH_MODE")){
						SkippReader(reader,2);
						XMLEvent nextVal = reader.nextEvent();
						if (nextVal.isStartElement()){
							startElement = nextVal.asStartElement();
							if(startElement.getName().getLocalPart().equals("Value")){
								transaction.setAUTH_MODE(reader.nextEvent().toString());
							}
						}
					}else if(paramCode.equals("FX_RATE")){
						SkippReader(reader,2);
						XMLEvent nextVal = reader.nextEvent();
						if (nextVal.isStartElement()){
							startElement = nextVal.asStartElement();
							if(startElement.getName().getLocalPart().equals("Value")){
								transaction.setFX_RATE(reader.nextEvent().toString());
							}
						}
					}else if(paramCode.equals("CARD_ABSENT")){
						SkippReader(reader,2);
						XMLEvent nextVal = reader.nextEvent();
						if (nextVal.isStartElement()){
							startElement = nextVal.asStartElement();
							if(startElement.getName().getLocalPart().equals("Value")){
								transaction.setCARD_ABSENT(reader.nextEvent().toString());
							}
						}
					}
					else if (paramCode.equals("BCURR")){
						SkippReader(reader,2);
						XMLEvent nextVal = reader.nextEvent();
						if (nextVal.isStartElement()){
							startElement = nextVal.asStartElement();
							if(startElement.getName().getLocalPart().equals("Value")){
								transaction.setBCURR(reader.nextEvent().toString());
							}
						}
					}else if(paramCode.equals("SG")){
						SkippReader(reader,2);
						XMLEvent nextVal = reader.nextEvent();
						if (nextVal.isStartElement()){
							startElement = nextVal.asStartElement();
							if(startElement.getName().getLocalPart().equals("Value")){
								transaction.setSG(reader.nextEvent().toString());
							}
						}
					}else if(paramCode.equals("WHAT_TRX")){
						SkippReader(reader,2);
						XMLEvent nextVal = reader.nextEvent();
						if (nextVal.isStartElement()){
							startElement = nextVal.asStartElement();
							if(startElement.getName().getLocalPart().equals("Value")){
								transaction.setWHAT_TRX(reader.nextEvent().toString());
							}
						}
					}else if(paramCode.equals("NEXT_AMOUNT")){
						SkippReader(reader,2);
						XMLEvent nextVal = reader.nextEvent();
						if (nextVal.isStartElement()){
							startElement = nextVal.asStartElement();
							if(startElement.getName().getLocalPart().equals("Value")){
								transaction.setNEXT_AMOUNT(reader.nextEvent().toString());
							}
						}
					}else if(paramCode.equals("BRAND")){
						SkippReader(reader,2);
						XMLEvent nextVal = reader.nextEvent();
						if (nextVal.isStartElement()){
							startElement = nextVal.asStartElement();
							if(startElement.getName().getLocalPart().equals("Value")){
								transaction.setBRAND(reader.nextEvent().toString());
							}
						}
					}else if(paramCode.equals("FIN_CURR")){
						SkippReader(reader,2);
						XMLEvent nextVal = reader.nextEvent();
						if (nextVal.isStartElement()){
							startElement = nextVal.asStartElement();
							if(startElement.getName().getLocalPart().equals("Value")){
								transaction.setFIN_CURR(reader.nextEvent().toString());
							}
						}
					}else if(paramCode.equals("NEXT_CURR")){
						SkippReader(reader,2);
						XMLEvent nextVal = reader.nextEvent();
						if (nextVal.isStartElement()){
							startElement = nextVal.asStartElement();
							if(startElement.getName().getLocalPart().equals("Value")){
								transaction.setNEXT_CURR(reader.nextEvent().toString());
							}
						}
					}else if(paramCode.equals("NEXT_CH")){
						SkippReader(reader,2);
						XMLEvent nextVal = reader.nextEvent();
						if (nextVal.isStartElement()){
							startElement = nextVal.asStartElement();
							if(startElement.getName().getLocalPart().equals("Value")){
								transaction.setNEXT_CH(reader.nextEvent().toString());
							}
						}
					}else if(paramCode.equals("PROC_CLASS")){
						SkippReader(reader,2);
						XMLEvent nextVal = reader.nextEvent();
						if (nextVal.isStartElement()){
							startElement = nextVal.asStartElement();
							if(startElement.getName().getLocalPart().equals("Value")){
								transaction.setPROC_CLASS(reader.nextEvent().toString());
							}
						}
					}else if(paramCode.equals("CPID")){
						SkippReader(reader,2);
						XMLEvent nextVal = reader.nextEvent();
						if (nextVal.isStartElement()){
							startElement = nextVal.asStartElement();
							if(startElement.getName().getLocalPart().equals("Value")){
								transaction.setCPID(reader.nextEvent().toString());
							}
						}
					}
				}
			}
			if (nextEvent.isEndElement() && nextEvent.asEndElement().getName().toString().equals("Transaction")) {
				publicDoc.setTransaction(transaction);
				return publicDoc;
			}
		}
		return publicDoc;
	}

	private static  DocClass ReadBilling(DocClass publicDoc,XMLEventReader reader) throws XMLStreamException{
		Billing billing = new Billing();
		while(reader.hasNext()){
			XMLEvent nextEvent = reader.nextEvent();
			if (nextEvent.isStartElement()) {
				StartElement startElement = nextEvent.asStartElement();
				if(startElement.getName().getLocalPart().equals("PhaseDate")){
					billing.setPhaseDate(reader.nextEvent().toString());
				}else if(startElement.getName().getLocalPart().equals("Currency")){
					billing.setCurrency(reader.nextEvent().toString());
				}else if(startElement.getName().getLocalPart().equals("Amount")){
					billing.setAmount(reader.nextEvent().toString());
				}
			}
			if (nextEvent.isEndElement() && nextEvent.asEndElement().getName().toString().equals("Billing")) {
				publicDoc.setBilling(billing);
				return publicDoc;
			}
		}
		return publicDoc;
	}
	private static  DocClass ReadReconciliation(DocClass publicDoc,XMLEventReader reader) throws XMLStreamException{
		Reconciliation reconciliation = new Reconciliation();
		while(reader.hasNext()){
			XMLEvent nextEvent = reader.nextEvent();
			if (nextEvent.isStartElement()) {
				StartElement startElement = nextEvent.asStartElement();
				if(startElement.getName().getLocalPart().equals("PhaseDate")){
					reconciliation.setPhaseDate(reader.nextEvent().toString());
				}else if(startElement.getName().getLocalPart().equals("Currency")){
					reconciliation.setCurrency(reader.nextEvent().toString());
				}else if(startElement.getName().getLocalPart().equals("Amount")){
					reconciliation.setAmount(reader.nextEvent().toString());
				}
			}
			if (nextEvent.isEndElement() && nextEvent.asEndElement().getName().toString().equals("Reconciliation")) {
				publicDoc.setReconciliation(reconciliation);
				return publicDoc;
			}
		}
		return publicDoc;
	}
	private static  DocClass ReadStatus(DocClass publicDoc,XMLEventReader reader) throws XMLStreamException{
		Status status = new Status();
		while(reader.hasNext()){
			XMLEvent nextEvent = reader.nextEvent();
			if (nextEvent.isStartElement()) {
				StartElement startElement = nextEvent.asStartElement();
				if(startElement.getName().getLocalPart().equals("RespClass")){
					status.setRespClass(reader.nextEvent().toString());
				}else if(startElement.getName().getLocalPart().equals("RespCode")){
					status.setRespCode(reader.nextEvent().toString());
				}else if(startElement.getName().getLocalPart().equals("RespText")){
					status.setRespText(reader.nextEvent().toString());
				}else if(startElement.getName().getLocalPart().equals("PostingStatus")){
					status.setPostingStatus(reader.nextEvent().toString());
				}
			}
			if (nextEvent.isEndElement() && nextEvent.asEndElement().getName().toString().equals("Status")) {
				publicDoc.setStatus(status);
				return publicDoc;
			}
		}
		return publicDoc;
	}
	private static  void SkippReader(XMLEventReader reader,int num) throws XMLStreamException{
	 while(num--!=0){
		 reader.nextEvent();
	 }
 }

 private static void MakeSqlOperation(Connection con) throws SQLException {
	 System.out.println();
	 System.out.println("Report Database processing Start.....");
	 long DBReProcessStartTime = System.currentTimeMillis();
	 Statement st = con.createStatement();
	 CallableStatement callSt = con.prepareCall( "{call sp_report()}");
	 callSt.execute();
	
	 long DBReProcessEndTime = System.currentTimeMillis();
	 System.out.println("Report Database processing time: "+(DBReProcessEndTime-DBReProcessStartTime)/1000 +" seconds");
	 System.out.println("Report Database processing End.");
	 String sql_group = null;
	 sql_group = "select Date(LocalDt) as LocalDt from par_normal_temp group by Date(LocalDt)";

	 String TableName = null;
	 //string sql_del_recs_normal = CreateMyTable(TableName);
	 ResultSet result = st.executeQuery(sql_group);
	 int res = 1;

	 System.out.println("Table processing Start.....");
	 int created = 0;
	 int renamed = 0;
	 /* For inserting in a single table Start */
	/* try{
		 String sqlCreate = "INSERT INTO `transactions` SELECT * FROM par_normal_temp WHERE MsgCode='null'  GROUP BY RRN,CreationDate,OrgMemberId,DestMemberId";
		 //System.out.println(sqlCreate);
		 Statement stCr = con.createStatement();
		 stCr.execute(sqlCreate);

		 sqlCreate = "DELETE FROM par_normal_temp WHERE MsgCode='null' ";
		 stCr.execute(sqlCreate);
	 }catch (Exception e){
		 System.out.println(e.toString());
	 }*/
	 /* For inserting in a single table End */

	 while(result.next()){
		 System.out.flush();
		 System.out.print("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b");

		 TableName = result.getString("LocalDt");
         Statement stCr = con.createStatement();

		 String sqlCreate = "SHOW TABLES LIKE '"+TableName+"'";
		 ResultSet  exist = stCr.executeQuery(sqlCreate);
		 if(exist.next()){
			 sqlCreate = "alter table `"+TableName+"` rename to `"+TableName+"_"+dateNow+"`";
			 stCr.execute(sqlCreate);
			 System.out.print("Table Renamed "+ ++renamed);
		 }else{
			 System.out.print("Table Created "+ ++created);
		 }
		 sqlCreate = CreateMyTable(TableName);
		 stCr.execute(sqlCreate);
		 //Console.WriteLine("\nDone .. " + String.Format("{0:yyyyMMdd}", dt_group.Rows[j]["LocalDt"]));

		 sqlCreate = "INSERT INTO `" + TableName + "` SELECT * FROM par_normal_temp WHERE MsgCode='null' and Date(LocalDt)= '" + TableName + "' GROUP BY RRN,OrgMemberId,DestMemberId";
		 //System.out.println(sqlCreate);
		 stCr.execute(sqlCreate);

		 sqlCreate = "DELETE FROM par_normal_temp WHERE MsgCode='null' and Date(LocalDt)='" + TableName + "'";
		 stCr.execute(sqlCreate);
	 }
	 System.out.println();
	 System.out.println("Table processing End.");
	 System.out.println("Table Created "+created);
	 System.out.println("Table Renamed "+renamed);
	 callSt = con.prepareCall( "{call sp_clean()}");
	 callSt.execute();
 }
	private static String CreateMyTable(String tableName)
	{
		StringBuilder sb = new StringBuilder();
		//sb.Append("DROP TABLE IF EXISTS `" + tableName + "`;");
		sb.append("CREATE TABLE IF NOT EXISTS `");
		sb.append(tableName);
		sb.append("` (  `Sender` varchar(255) NOT NULL," +
				"`CreationDate` varchar(255) DEFAULT NULL," +
				"`CreationTime` varchar(255) DEFAULT NULL," +
				"`FileSeqNumber` int(11) DEFAULT NULL," +
				"`Receiver` varchar(50) DEFAULT NULL," +
				"`MsgCode` varchar(50) DEFAULT NULL," +
				"`ServiceClass` varchar(50) DEFAULT NULL," +
				"`DRN` varchar(50) DEFAULT NULL," +
				"`SRN` varchar(50) DEFAULT NULL," +
				"`RRN` varchar(50) NOT NULL DEFAULT ''," +
				"`ARN` varchar(50) DEFAULT NULL," +
				"`AuthCode` varchar(50) DEFAULT NULL," +
				"`LocalDt` varchar(255) DEFAULT NULL," +
				"`NWDt` varchar(255) DEFAULT NULL," +
				"`SIC` varchar(50) DEFAULT NULL," +
				"`Country` varchar(50) DEFAULT NULL," +
				"`City` varchar(50) DEFAULT NULL," +
				"`MerchantName` varchar(100) DEFAULT NULL," +
				"`MerchantID` varchar(50) DEFAULT NULL," +
				"`OrgContractNumber` varchar(50) DEFAULT NULL," +
				"`OrgMemberId` varchar(50) DEFAULT NULL," +
				"`OrgTransitId` varchar(50) DEFAULT NULL," +
				"`OrgChannel` varchar(50) DEFAULT NULL," +
				"`DestContractNumber` varchar(50) DEFAULT NULL," +
				"`DestContractNumberMask` varchar(255) DEFAULT NULL," +
				"`DestMemberId` varchar(50) DEFAULT NULL," +
				"`DestTransitId` varchar(50) DEFAULT NULL," +
				"`DestRelation` varchar(50) DEFAULT NULL," +
				"`DestChannel` varchar(50) DEFAULT NULL," +
				"`TranCurrency` varchar(50) DEFAULT NULL," +
				"`TranAmount` varchar(50) DEFAULT NULL," +
				"`WhatTrx` varchar(50) DEFAULT NULL," +
				"`CpId` varchar(50) DEFAULT NULL," +
				"`BillPhaseDate` varchar(255) DEFAULT NULL," +
				"`BillCurrency` varchar(50) DEFAULT NULL," +
				"`BillAmount` varchar(50) DEFAULT NULL," +
				"`ReconPhaseDate` varchar(255) DEFAULT NULL," +
				"`ReconCurrency` varchar(50) DEFAULT NULL," +
				"`ReconAmount` varchar(50) DEFAULT NULL," +
				"`RecsCount` varchar(50) DEFAULT NULL," +
				"`HashTotalAmount` varchar(50) DEFAULT NULL," +
				"`Flag` int(10) DEFAULT '0'," +
				"`MyKeyStatus` varchar(45) DEFAULT NULL," +
				"`DMSstatusDT` varchar(45) DEFAULT NULL," +
				"PRIMARY KEY (`RRN`,`OrgMemberId`,`DestMemberId`)," +
				"UNIQUE KEY `RRN` (`RRN`,`OrgMemberId`,`DestMemberId`))");

		return sb.toString();
	}
	private static String CreateTable2(String tableName)
	{
		StringBuilder sb = new StringBuilder();
		//sb.Append("DROP TABLE IF EXISTS `" + tableName + "`;");
		sb.append("CREATE TABLE IF NOT EXISTS `");
		sb.append(tableName);
		sb.append("` (  `Sender` varchar(255) NOT NULL," +
				"`CreationDate` varchar(255) DEFAULT NULL," +
				"`CreationTime` varchar(255) DEFAULT NULL," +
				"`FileSeqNumber` int(11) DEFAULT NULL," +
				"`Receiver` varchar(50) DEFAULT NULL," +
				"`MsgCode` varchar(50) DEFAULT NULL," +
				"`ServiceClass` varchar(50) DEFAULT NULL," +
				"`DRN` varchar(50) DEFAULT NULL," +
				"`SRN` varchar(50) DEFAULT NULL," +
				"`RRN` varchar(50) NOT NULL DEFAULT ''," +
				"`ARN` varchar(50) DEFAULT NULL," +
				"`AuthCode` varchar(50) DEFAULT NULL," +
				"`LocalDt` varchar(255) DEFAULT NULL," +
				"`NWDt` varchar(255) DEFAULT NULL," +
				"`SIC` varchar(50) DEFAULT NULL," +
				"`Country` varchar(50) DEFAULT NULL," +
				"`City` varchar(50) DEFAULT NULL," +
				"`MerchantName` varchar(100) DEFAULT NULL," +
				"`MerchantID` varchar(50) DEFAULT NULL," +
				"`OrgContractNumber` varchar(50) DEFAULT NULL," +
				"`OrgMemberId` varchar(50) DEFAULT NULL," +
				"`OrgTransitId` varchar(50) DEFAULT NULL," +
				"`OrgChannel` varchar(50) DEFAULT NULL," +
				"`DestContractNumber` varchar(50) DEFAULT NULL," +
				"`DestContractNumberMask` varchar(255) DEFAULT NULL," +
				"`DestMemberId` varchar(50) DEFAULT NULL," +
				"`DestTransitId` varchar(50) DEFAULT NULL," +
				"`DestRelation` varchar(50) DEFAULT NULL," +
				"`DestChannel` varchar(50) DEFAULT NULL," +
				"`TranCurrency` varchar(50) DEFAULT NULL," +
				"`TranAmount` varchar(50) DEFAULT NULL," +
				"`WhatTrx` varchar(50) DEFAULT NULL," +
				"`CpId` varchar(50) DEFAULT NULL," +
				"`BillPhaseDate` varchar(255) DEFAULT NULL," +
				"`BillCurrency` varchar(50) DEFAULT NULL," +
				"`BillAmount` varchar(50) DEFAULT NULL," +
				"`ReconPhaseDate` varchar(255) DEFAULT NULL," +
				"`ReconCurrency` varchar(50) DEFAULT NULL," +
				"`ReconAmount` varchar(50) DEFAULT NULL," +
				"`RecsCount` varchar(50) DEFAULT NULL," +
				"`HashTotalAmount` varchar(50) DEFAULT NULL," +
				"`Flag` int(10) DEFAULT '0'," +
				"`MyKeyStatus` varchar(45) DEFAULT NULL," +
				"`DMSstatusDT` varchar(45) DEFAULT NULL)");


		return sb.toString();
	}

	private static void Create_SP_Clean(Connection con) throws SQLException {

		Statement st = con.createStatement();
		String query = "CREATE PROCEDURE IF not EXISTS sp_clean() ";
		query+="BEGIN ";
		query+=" INSERT INTO par_dump SELECT * FROM par_normal_temp; ";
		query+=" DELETE FROM par_normal_temp; ";
		query+=" INSERT INTO par_dump SELECT * FROM par_normal_ext; "+" DELETE FROM par_normal_ext; ";
		query+=" DELETE FROM par_mtemp; ";
		query+="END";
		//System.out.println(query);
		st.execute(query);
		st.close();
	}
	private static void Create_SP_Report(Connection con) throws SQLException {
		String query=" CREATE PROCEDURE IF NOT EXISTS sp_report() \n"+
		" BEGIN \n"+
		" INSERT INTO par_mtemp SELECT * FROM par_normal_temp; \n"+
		" DELETE FROM par_normal_temp WHERE RRN IN (SELECT RRN FROM par_mtemp WHERE MsgCode='01000B'); \n"+
		" INSERT INTO par_normal_ext SELECT * FROM par_normal_temp WHERE RRN IN (SELECT RRN FROM par_normal_temp WHERE MsgCode='04200A'); \n"+
		" DELETE FROM par_normal_temp WHERE RRN IN (SELECT RRN FROM par_mtemp WHERE MsgCode='04200A'); \n"+
		" INSERT INTO par_orphan_rev SELECT * FROM par_normal_ext WHERE MsgCode='04200A' GROUP BY RRN, OrgMemberId, DestMemberId HAVING count(*)=1; \n"+
		" DELETE FROM par_normal_ext WHERE RRN IN (SELECT RRN FROM par_mtemp WHERE MsgCode='04200A'); \n"+
		" INSERT INTO par_normal_ext SELECT * FROM par_normal_temp WHERE RRN IN (SELECT RRN FROM par_normal_temp WHERE MsgCode='04200R'); \n"+
		" DELETE FROM par_normal_temp WHERE RRN IN (SELECT RRN FROM par_mtemp WHERE MsgCode='04200R'); \n"+
		" INSERT INTO par_orphan_rev SELECT * FROM par_normal_ext WHERE MsgCode='04200R' GROUP BY RRN, OrgMemberId, DestMemberId HAVING count(*)=1; \n"+
		" DELETE FROM par_normal_ext WHERE RRN IN (SELECT RRN FROM par_mtemp WHERE MsgCode='04200R'); \n"+
		" DELETE FROM par_normal_temp WHERE RRN IN (SELECT RRN FROM par_mtemp WHERE MsgCode='93000S'); \n"+
		" INSERT INTO par_rec_dispute SELECT * FROM par_normal_temp WHERE MsgCode LIKE 'D%'; \n"+
		" DELETE FROM par_normal_temp WHERE MsgCode LIKE 'D%'; \n"+
		" END";
		//System.out.println(query);
		Statement st = con.createStatement();
		st.execute(query);
	}

	private static String getMasked(String detContract ){
		StringBuilder sb = new StringBuilder();
		sb.append(detContract);
		int start = 6; int end = 4;
		int mid = (sb.toString().length()-end)-start;
		for(int i=6;i<mid+7;i++){
			sb.replace(i,i+1,"*");
		}
		return sb.toString();
	}
}
