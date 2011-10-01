package edu.wustl.catissuecore.testcase.bizlogic;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;

public class SiteAssociation extends CaTissueSuiteBaseTest {

	private static int rowNo = 1;

	public SiteAssociation() {
		super();
	}

	protected void setUserSiteCP(String excel[][]) throws Exception {
		try {
			System.out.println("In setUser excel.lenght " + excel.length);
			while (rowNo < excel.length - 1) {
				System.out.println("rowNo " + rowNo);
				User user = new User();
				user.setLoginName(excel[rowNo][0]);
				System.out.println("loginName " + excel[rowNo][0]);
				List<?> userResultList = null;
				List<?> siteResultList = null;
				//userResultList = UserBizTestCases.appService.search(User.class,user);
			 String query = "from edu.wustl.catissuecore.domain.User as user where "
	 				+ "user.loginName=' "+excel[rowNo][0]+"'";	
	        	  userResultList = appService.search(query);
				System.out.println("userResultList.size() "
						+ userResultList.size());
				if (userResultList.size() != 0) {
					System.out.println("No of Users retrived from DB "
							+ userResultList.size());
					user = (User) userResultList.get(0);

					Site site = new Site();
					System.out.println("siteName " + excel[rowNo][1]);
					site.setName(excel[rowNo][1]);

					//siteResultList = SiteBizTestCases.appService.search(	Site.class, site);
					//userResultList = UserBizTestCases.appService.search(User.class,user);
					  query = "from edu.wustl.catissuecore.domain.Site as site where "
			 				+ "site.name=' "+excel[rowNo][1]+"'";	
			         siteResultList = appService.search(query);
					if (siteResultList.size() != 0) {

						System.out.println("No of Sites retrived from DB "
								+ siteResultList.size());
						site = (Site) siteResultList.get(0);
					}

					Collection<Site> siteCollection = new HashSet<Site>();
					siteCollection.add(site);

					user.setSiteCollection(siteCollection);
					appService.updateObject(user);
					System.out.println("user updated " + user + " " + rowNo);
					System.out.println("excel[rowNo][2]  " + excel[rowNo][2]);
					if (excel[rowNo][2] != null && !excel[rowNo][2].equals("")) {
						List<?> cPResultList = null;
						String cpShortTitle = excel[rowNo][2].trim();
						System.out.println("CP ShortTitle " + cpShortTitle);
						// System.out.println(cpShortTitle.contains(","));

						if (cpShortTitle.contains(",")) {
							String str[] = excel[rowNo][2].toString()
									.split(",");
							System.out.println("No of cps " + str.length);
							int i = 0;
							while (str.length > i) {
								cpShortTitle = str[i].trim();
								System.out.println("cp  " + cpShortTitle);
								CollectionProtocol collectionProtocol = new CollectionProtocol();
								collectionProtocol.setShortTitle(cpShortTitle);
								//cPResultList = CollectionProtocolBizTestCases.appService.search(CollectionProtocol.class,collectionProtocol);
								query = "from edu.wustl.catissuecore.domain.CollectionProtocol as collectionProtocol where "
					 				+ "collectionProtocol.shortTitle=' "+cpShortTitle+"'";	
								cPResultList = appService.search(query);
								if (cPResultList.size() != 0) {

									System.out
											.println("No of CP retrived from DB "
													+ cPResultList.size());
									collectionProtocol = (CollectionProtocol) cPResultList
											.get(0);
									collectionProtocol
											.setSiteCollection(siteCollection);
									CollectionProtocolBizTestCases.appService
											.updateObject(collectionProtocol);
									System.out.println("cp updated");
								}
								i++;
							}
						} else {
							CollectionProtocol collectionProtocol = new CollectionProtocol();
							collectionProtocol.setShortTitle(cpShortTitle);
							System.out.println("CP Short Title  "
									+ cpShortTitle);
							//cPResultList = CollectionProtocolTestCases.appService.search(CollectionProtocol.class,collectionProtocol);
							query = "from edu.wustl.catissuecore.domain.CollectionProtocol as collectionProtocol where "
				 				+ "collectionProtocol.shortTitle=' "+cpShortTitle+"'";	
							cPResultList = appService.search(query);
							System.out.println("cPResultList.size() "
									+ cPResultList.size());
							if (cPResultList.size() != 0) {
								System.out
										.println("No of CPs retrived from DB "
												+ cPResultList.size());
								collectionProtocol = (CollectionProtocol) cPResultList
										.get(0);
								System.out.println("collectionProtocol "
										+ collectionProtocol.getShortTitle());
								collectionProtocol
										.setSiteCollection(siteCollection);
								CollectionProtocolBizTestCases.appService
										.updateObject(collectionProtocol);
								System.out.println("cp updated");
							}

						}
					}
				}
				rowNo++;
			}
		} catch (Exception ex) {
			System.out.println("Exception in setUserSiteCP");
			ex.printStackTrace();
			throw ex;
		}
	}

	protected void setUserSite(String excel[][]) throws Exception {
		try {
			System.out.println("In setUserSite excel.lenght " + excel.length);
			while (rowNo < excel.length) {
				User user = new User();
				user.setLoginName(excel[rowNo][0]);
				System.out.println("loginName " + excel[rowNo][0]);
				List<?> userResultList = null;
				List<?> siteResultList = null;
				//userResultList = UserTestBizCases.appService.search(User.class,	user);
				String query = "from edu.wustl.catissuecore.domain.User as user where "
	 				+ "user.loginName=' "+excel[rowNo][0]+"'";	
	        	  userResultList = appService.search(query);
				System.out.println("userResultList.size() "
						+ userResultList.size());
				if (userResultList.size() != 0) {
					System.out.println("No of Users retrived from DB "
							+ userResultList.size());
					user = (User) userResultList.get(0);

					Site site = new Site();
					site.setName(excel[rowNo][1]);

					//siteResultList = SiteBizTestCases.appService.search(Site.class, site);
					query = "from edu.wustl.catissuecore.domain.Site as site where "
		 				+ "site.name=' "+excel[rowNo][1]+"'";	
		          siteResultList = appService.search(query);
					if (siteResultList.size() != 0) {
						System.out.println("No of Sites retrived from DB "
								+ siteResultList.size());
						site = (Site) siteResultList.get(0);
					}

					Collection<Site> siteCollection = new HashSet<Site>();
					siteCollection.add(site);
					user.setSiteCollection(siteCollection);
					user = (User) appService.updateObject(user);
					System.out.println("user updated");
				}
				rowNo++;
			}
		} catch (Exception ex) {
			System.out.println("Exception in setUserSite");
			ex.printStackTrace();
			throw ex;
		}
	}
	/*
	protected void setCPSite(String excel[][]) throws Exception {
		try {
			System.out.println("In setCPSite excel.lenght " + excel.length);
			while (rowNo < excel.length - 1) {
				System.out.println("rowNo " + rowNo);
				CollectionProtocol collectionProtocol = new CollectionProtocol();
				String shortTitle = excel[rowNo][2];
				collectionProtocol.setShortTitle(shortTitle);
				System.out.println("CP Short Title  " + shortTitle);
				List<?> cPResultList = null;
				cPResultList = CollectionProtocolTestCases.appService.search(
						CollectionProtocol.class, collectionProtocol);
				System.out
						.println("cPResultList.size() " + cPResultList.size());
				if (cPResultList.size() != 0) {
					System.out.println("No of CPs retrived from DB "
							+ cPResultList.size());
					collectionProtocol = (CollectionProtocol) cPResultList
							.get(0);

					Site site = new Site();
					site.setName(excel[rowNo][1]);
					System.out.println("Site Name is " + excel[rowNo][1]);
					List<?> siteResultList = null;
					siteResultList = SiteTestCases.appService.search(
							Site.class, site);
					if (siteResultList.size() != 0) {
						System.out.println("No of Sites retrived from DB "
								+ siteResultList.size());
						site = (Site) siteResultList.get(0);
						Collection<Site> siteCollection = new HashSet<Site>();
						siteCollection.add(site);
						collectionProtocol.setSiteCollection(siteCollection);
					}

					CollectionProtocolTestCases.appService
							.updateObject(collectionProtocol);
					System.out.println("cp updated");
				}
				rowNo++;
			}
		} catch (Exception ex) {
			System.out.println("Exception in setCPSite  ");
			ex.printStackTrace();
			throw ex;
		}
	}
*/
}
