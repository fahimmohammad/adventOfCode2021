public class DocClass {
	
	String Sender;
	String CreationDate;
    String CreationTime;
    int FileSeqNumber;
    String Receiver;
    int docCount=0;
    String RecsCount;
    String HashTotalAmount;
    String MsgCode;
    String ServiceClass;
    String RequestCategory;
    String TransTypeCode;
    String SRN;
    String DRN;
    String RRN;
    String ARN;
    String PrevDRN;
    String OrigDRN;
    String AuthCode;
    String LocalDt;
    String NWDt;
    String SRVC;
    String WHAT_TRX;
    String CPID;
    String OrgContractNumber;
    String OrgMemberId;
    String OrgTransitId;
    String OrgChannel;
    String DestContractNumber;
    String DestContractNumberMask;
    String DestMemberId;
    String DestTransitId;
    String DestRelation;
    String DestChannel;
    String TranCurrency;
    String TranAmount;
    String BillPhaseDate;
    String BillCurrency;
    String BillAmount;
    String ReconPhaseDate;
    String ReconCurrency;
    String ReconAmount;
	String Institution;
	String MemberId;
	SourceDetails SourceDetails;
	Originator Originator;
	Destination Destination;
	Transaction Transaction;
	Billing Billing;
	Reconciliation Reconciliation;
	Status Status;
	FileHeader FileHeader;
	FileTrailer FileTrailer;
    
    
    public String getPrevDRN() {
		return PrevDRN;
	}
	public void setPrevDRN(String prevDRN) {
		PrevDRN = prevDRN;
	}
	public String getOrigDRN() {
		return OrigDRN;
	}
	public void setOrigDRN(String origDRN) {
		OrigDRN = origDRN;
	}
	public String getSender() {
		return Sender;
	}
	public void setSender(String sender) {
		Sender = sender;
	}
	public String getCreationDate() {
		return CreationDate;
	}
	public void setCreationDate(String creationDate) {
		CreationDate = creationDate;
	}
	public String getCreationTime() {
		return CreationTime;
	}
	public void setCreationTime(String creationTime) {
		CreationTime = creationTime;
	}
	public int getFileSeqNumber() {
		return FileSeqNumber;
	}
	public void setFileSeqNumber(int fileSeqNumber) {
		FileSeqNumber = fileSeqNumber;
	}
	public String getReceiver() {
		return Receiver;
	}
	public void setReceiver(String receiver) {
		Receiver = receiver;
	}
	public int getDocCount() {
		return docCount;
	}
	public void setDocCount(int docCount) {
		this.docCount = docCount;
	}
	public String getRecsCount() {
		return RecsCount;
	}
	public void setRecsCount(String recsCount) {
		RecsCount = recsCount;
	}
	public String getHashTotalAmount() {
		return HashTotalAmount;
	}
	public void setHashTotalAmount(String hashTotalAmount) {
		HashTotalAmount = hashTotalAmount;
	}
	public String getMsgCode() {
		return MsgCode;
	}
	public void setMsgCode(String msgCode) {
		MsgCode = msgCode;
	}
	public String getServiceClass() {
		return ServiceClass;
	}
	public void setServiceClass(String serviceClass) {
		ServiceClass = serviceClass;
	}
	public String getRequestCategory() {
		return RequestCategory;
	}
	public void setRequestCategory(String requestCategory) {
		RequestCategory = requestCategory;
	}
	public String getTransTypeCode() {
		return TransTypeCode;
	}
	public void setTransTypeCode(String transTypeCode) {
		TransTypeCode = transTypeCode;
	}
	public String getSRN() {
		return SRN;
	}
	public void setSRN(String sRN) {
		SRN = sRN;
	}
	public String getDRN() {
		return DRN;
	}
	public void setDRN(String dRN) {
		DRN = dRN;
	}
	public String getRRN() {
		return RRN;
	}
	public void setRRN(String rRN) {
		RRN = rRN;
	}
	public String getARN() {
		return ARN;
	}
	public void setARN(String aRN) {
		ARN = aRN;
	}
	public String getAuthCode() {
		return AuthCode;
	}
	public void setAuthCode(String authCode) {
		AuthCode = authCode;
	}
	public String getLocalDt() {
		return LocalDt;
	}
	public void setLocalDt(String localDt) {
		LocalDt = localDt;
	}
	public String getNWDt() {
		return NWDt;
	}
	public void setNWDt(String nWDt) {
		NWDt = nWDt;
	}
	public String getSRVC() {
		return SRVC;
	}
	public void setSRVC(String sRVC) {
		SRVC = sRVC;
	}
	public String getWHAT_TRX() {
		return WHAT_TRX;
	}
	public void setWHAT_TRX(String wHAT_TRX) {
		WHAT_TRX = wHAT_TRX;
	}
	public String getCPID() {
		return CPID;
	}
	public void setCPID(String cPID) {
		CPID = cPID;
	}
	public String getOrgContractNumber() {
		return OrgContractNumber;
	}
	public void setOrgContractNumber(String orgContractNumber) {
		OrgContractNumber = orgContractNumber;
	}
	public String getOrgMemberId() {
		return OrgMemberId;
	}
	public void setOrgMemberId(String orgMemberId) {
		OrgMemberId = orgMemberId;
	}
	public String getOrgTransitId() {
		return OrgTransitId;
	}
	public void setOrgTransitId(String orgTransitId) {
		OrgTransitId = orgTransitId;
	}
	public String getOrgChannel() {
		return OrgChannel;
	}
	public void setOrgChannel(String orgChannel) {
		OrgChannel = orgChannel;
	}
	public String getDestContractNumber() {
		return DestContractNumber;
	}
	public void setDestContractNumber(String destContractNumber) {
		DestContractNumber = destContractNumber;
	}
	public String getDestContractNumberMask() {
		return DestContractNumberMask;
	}
	public void setDestContractNumberMask(String destContractNumberMask) {
		DestContractNumberMask = destContractNumberMask;
	}
	public String getDestMemberId() {
		return DestMemberId;
	}
	public void setDestMemberId(String destMemberId) {
		DestMemberId = destMemberId;
	}
	public String getDestTransitId() {
		return DestTransitId;
	}
	public void setDestTransitId(String destTransitId) {
		DestTransitId = destTransitId;
	}
	public String getDestRelation() {
		return DestRelation;
	}
	public void setDestRelation(String destRelation) {
		DestRelation = destRelation;
	}
	public String getDestChannel() {
		return DestChannel;
	}
	public void setDestChannel(String destChannel) {
		DestChannel = destChannel;
	}
	public String getTranCurrency() {
		return TranCurrency;
	}
	public void setTranCurrency(String tranCurrency) {
		TranCurrency = tranCurrency;
	}
	public String getTranAmount() {
		return TranAmount;
	}
	public void setTranAmount(String tranAmount) {
		TranAmount = tranAmount;
	}
	public String getBillPhaseDate() {
		return BillPhaseDate;
	}
	public void setBillPhaseDate(String billPhaseDate) {
		BillPhaseDate = billPhaseDate;
	}
	public String getBillCurrency() {
		return BillCurrency;
	}
	public void setBillCurrency(String billCurrency) {
		BillCurrency = billCurrency;
	}
	public String getBillAmount() {
		return BillAmount;
	}
	public void setBillAmount(String billAmount) {
		BillAmount = billAmount;
	}
	public String getReconPhaseDate() {
		return ReconPhaseDate;
	}
	public void setReconPhaseDate(String reconPhaseDate) {
		ReconPhaseDate = reconPhaseDate;
	}
	public String getReconCurrency() {
		return ReconCurrency;
	}
	public void setReconCurrency(String reconCurrency) {
		ReconCurrency = reconCurrency;
	}
	public String getReconAmount() {
		return ReconAmount;
	}
	public void setReconAmount(String reconAmount) {
		ReconAmount = reconAmount;
	}
	public String getInstitution() {return Institution;}
	public void setInstitution(String institution) {Institution = institution;}

	public String getMemberId() {
		return MemberId;
	}
	public void setMemberId(String memberId) {
		MemberId = memberId;
	}

	public SourceDetails getSourceDetails() {
		return SourceDetails;
	}

	public void setSourceDetails(SourceDetails sourceDetails) {
		SourceDetails = sourceDetails;
	}

	public Originator getOriginator() {
		return Originator;
	}

	public void setOriginator(Originator originator) {
		Originator = originator;
	}

	public Destination getDestination() {
		return Destination;
	}

	public void setDestination(Destination destination) {
		Destination = destination;
	}

	public Transaction getTransaction() {
		return Transaction;
	}

	public void setTransaction(Transaction transaction) {
		Transaction = transaction;
	}

	public Billing getBilling() {
		return Billing;
	}

	public void setBilling(Billing billing) {
		Billing = billing;
	}

	public Reconciliation getReconciliation() {
		return Reconciliation;
	}

	public void setReconciliation(Reconciliation reconciliation) {
		Reconciliation = reconciliation;
	}

	public Status getStatus() {
		return Status;
	}

	public void setStatus(Status status) {
		Status = status;
	}

	public FileHeader getFileHeader() {
		return FileHeader;
	}

	public void setFileHeader(FileHeader fileHeader) {
		FileHeader = fileHeader;
	}

	public FileTrailer getFileTrailer() {
		return FileTrailer;
	}

	public void setFileTrailer(FileTrailer fileTrailer) {
		FileTrailer = fileTrailer;
	}
}

