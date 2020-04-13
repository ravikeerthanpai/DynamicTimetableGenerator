package DataSource;

import java.io.*;
import java.util.Iterator;
import java.util.List;

import org.dom4j.*;
import org.dom4j.io.SAXReader;

import DTTG.DTTG;
import DTTG.DTTGModule;


public class OPEN extends DTTGModule {
	private static final long serialVersionUID = 1L;

	public OPEN(DTTG dttg) {
		super(dttg, "", "");
	}

	@SuppressWarnings("unchecked")
	public void readData(String filename) {
		setDTTGData();
		Document doc;

		try {
			List<Element> innerEle;
			List<Element> groupElements;
			doc = new SAXReader().read(new File(filename));
			String _collageName =doc.getRootElement().attributeValue("College_Name");
			String _deptName =doc.getRootElement().attributeValue("Department_Name");
			getDTTGData().getRule().setCollageName(_collageName);
			getDTTGData().getRule().setDeptName(_deptName);
			System.out.println(_collageName);
			System.out.println(_deptName);
			XPath catPath = DocumentHelper.createXPath("//Teachers_List/Teacher");
			List<Element> catElements = catPath.selectNodes(doc);
			for (Iterator<Element> it = catElements.iterator(); it.hasNext();) {
				Element element = it.next();
				getDTTGData().addTeacher(element.elementText("Name"),element.elementText("Type"));
			}

			catPath = DocumentHelper.createXPath("//Subjects_List/Subject");
			catElements = catPath.selectNodes(doc);
			for (Iterator<Element> it = catElements.iterator(); it.hasNext();) {
				Element element = it.next();
				getDTTGData().addSubject(element.elementText("Name"),
						Integer.parseInt(element.elementText("No_Theory")),
						Integer.parseInt(element.elementText("No_Practical")));
			}

			catPath = DocumentHelper.createXPath("//Students_List/Student");
			catElements = catPath.selectNodes(doc);
			for (Iterator<Element> it = catElements.iterator(); it.hasNext();) {
				Element element = it.next();
				getDTTGData().addStudent(element.elementText("Name"),
						Integer.parseInt(element.elementText("Strength")));

				if (element.element("Group") != null) {
					groupElements = element.elements("Group");
					for (Iterator<Element> itg = groupElements.iterator(); itg.hasNext();) {
						Element Gelement = itg.next();
						//System.out.println(Gelement.getName());
						getDTTGData().addGroup(element.elementText("Name"),Gelement.elementText("Name"),Integer.parseInt(Gelement.elementText("Strength")));
					}
				}
			}

			catPath = DocumentHelper.createXPath("//Subjects_Alloc_List/SubjectAlloc");
			catElements = catPath.selectNodes(doc);

			if(getDTTGData().getAssigndSubjectLength()<getDTTGData().getSubjectLength())
				getDTTGData().copySubjToAssignSubj();
			int subjlen=0;
			int PR=0,TH=0;
			String s;
			for ( Iterator<Element> it = catElements.iterator(); it.hasNext();subjlen++) {
				Element element = it.next();
				String Subj=element.elementText("Name");
				//System.out.println(Subj);

				if (element.element("Theory") != null) {
					TH+=getDTTGData().getSubject(subjlen).getTPPW();
					Element Nelement = element.element("Theory");
					s=Nelement.elementText("Teacher");
					getDTTGData().getAssigndSubject(subjlen).setTchTH(s);
					//System.out.println("TH "+Nelement.elementText("Teacher"));
					
					s=Nelement.elementText("Student");
					getDTTGData().getAssigndSubject(subjlen).setStudTH(s);
					//System.out.println("TH "+Nelement.elementText("Student"));
				}
				
				if (element.element("Practical") != null) {
					groupElements = element.elements("Practical");
					for (Iterator<Element> itn = groupElements.iterator(); itn.hasNext();) {
						Element Nelement = itn.next();	
						innerEle=Nelement.elements("Teacher");
						for(int i=0;i<innerEle.size();i++){
							s=innerEle.get(i).getText();
							getDTTGData().getAssigndSubject(subjlen).getTchPR().add(s);
							//System.out.println("PR "+ s);
						}
						innerEle=Nelement.elements("Student");
						for(int i=0;i<innerEle.size();i++){
							PR++;//=getDTTGData().getSubject(index).getPPPW();
							s=innerEle.get(i).getText();
							getDTTGData().getAssigndSubject(subjlen).getStudPR().add(s);
							//System.out.println("PR "+ s);
						}						
					}
				}
			}
			getDTTGData().setTHPR(TH, PR);
			System.out.println(getDTTGData().getPR()+":0:"+getDTTGData().getTH());

			catPath = DocumentHelper.createXPath("//Rooms_List/Room");
			catElements = catPath.selectNodes(doc);
			for (Iterator<Element> it = catElements.iterator(); it.hasNext();) {
				Element element = it.next();
				getDTTGData().addRoom(element.elementText("Name"),
						Integer.parseInt(element.elementText("Capacity")),
						element.elementText("Type"));
			}

			catPath = DocumentHelper.createXPath("//Days_List/Day");
			catElements = catPath.selectNodes(doc);
			for (Iterator<Element> it = catElements.iterator(); it.hasNext();) {
				Element element = it.next();
				getDTTGData().getTimeSet().getDay().add(
						element.elementText("Name"));
			}

			catPath = DocumentHelper.createXPath("//Hours_List/Hour");
			catElements = catPath.selectNodes(doc);
			for (Iterator<Element> it = catElements.iterator(); it.hasNext();) {
				Element element = it.next();
				getDTTGData().getTimeSet().getHour().add(
						element.elementText("Name"));
			}

		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
	}
		
}
