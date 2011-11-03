package edu.wustl.catissuecore.bizlogic.ccts;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.PortInfo;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.spi.Provider;

import org.springframework.beans.factory.FactoryBean;

import edu.wustl.common.util.logger.Logger;

public abstract class ServiceClientFactoryBean implements FactoryBean {

	protected static final Logger logger = Logger
			.getCommonLogger(SubjectManagementFactoryBean.class);
	private String username;
	private String password;
	private String wsdlLocation;
	private String address;
	private boolean skipSSLVerification = true;

	public ServiceClientFactoryBean() {
		super();
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the wsdlLocation
	 */
	public String getWsdlLocation() {
		return wsdlLocation;
	}

	/**
	 * @param wsdlLocation
	 *            the wsdlLocation to set
	 */
	public void setWsdlLocation(String wsdlLocation) {
		this.wsdlLocation = wsdlLocation;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	protected HandlerResolver createHandlerResolver() {
		return new HandlerResolver() {
			@Override
			public List<Handler> getHandlerChain(PortInfo portInfo) {
				List<Handler> handlerChain = new ArrayList<Handler>();
				handlerChain.add(new SOAPHandler<SOAPMessageContext>() {
					public boolean handleMessage(SOAPMessageContext smc) {

						Boolean outboundProperty = (Boolean) smc
								.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

						if (outboundProperty.booleanValue()) {

							SOAPMessage message = smc.getMessage();

							try {

								SOAPEnvelope envelope = smc.getMessage()
										.getSOAPPart().getEnvelope();
								SOAPHeader header = envelope.addHeader();

								SOAPElement security = header
										.addChildElement(
												"Security",
												"wsse",
												"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");

								SOAPElement usernameToken = security
										.addChildElement("UsernameToken",
												"wsse");
								usernameToken
										.addAttribute(
												new QName("xmlns:wsu"),
												"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");

								SOAPElement username = usernameToken
										.addChildElement("Username", "wsse");
								username.addTextNode(getUsername());

								SOAPElement password = usernameToken
										.addChildElement("Password", "wsse");
								password.setAttribute(
										"Type",
										"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText");
								password.addTextNode(getPassword());

								// Print out the outbound SOAP message to
								// System.out
								// message.writeTo(System.out);
								// System.out.println("");

							} catch (Exception e) {
								logger.error(e, e);
							}

						}

						return outboundProperty;

					}

					public Set getHeaders() {
						return null;
					}

					public boolean handleFault(SOAPMessageContext context) {
						return true;
					}

					public void close(MessageContext context) {
					}
				});
				return handlerChain;
			}
		};
	}

	protected void setTimeouts(Map<String, Object> ctx) {
		ctx.put("com.sun.xml.internal.ws.connect.timeout", 30000);
		ctx.put("com.sun.xml.internal.ws.request.timeout", 30000);
		ctx.put("com.sun.xml.ws.connect.timeout", 30000);
		ctx.put("com.sun.xml.ws.request.timeout", 30000);
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

	protected void addSOAPLogger(BindingProvider bp) {
		try {
			javax.xml.ws.Binding binding = bp.getBinding();
			List handlerList = binding.getHandlerChain();
			if (handlerList == null) {
				handlerList = new ArrayList();
				binding.setHandlerChain(handlerList);
			}
			SOAPHandler loggingHandler = new SOAPHandler<SOAPMessageContext>() {
				public boolean handleMessage(SOAPMessageContext c) {
					SOAPMessage msg = c.getMessage();
					try {
						ByteArrayOutputStream out = new ByteArrayOutputStream();
						msg.writeTo(out);
						logger.info((out.toString()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					return true;
				}

				public boolean handleFault(SOAPMessageContext c) {
					SOAPMessage msg = c.getMessage();
					try {
						ByteArrayOutputStream out = new ByteArrayOutputStream();
						msg.writeTo(out);
						logger.info((out.toString()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					return true;
				}

				public void close(MessageContext c) {
				}

				public Set getHeaders() {
					return null;
				}
			};
			handlerList.add(loggingHandler);
		} catch (Exception e) {
			logger.error("Unable to add SOAP logger due to " + e.getMessage()
					+ ". Probably safe to ignore this.");
		}
	}

	/**
	 * @param port
	 * @return
	 */
	protected Object preparePort(BindingProvider port) {
		addSOAPLogger(port);
		final Map<String, Object> ctx = ((javax.xml.ws.BindingProvider) port)
				.getRequestContext();
		ctx.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, getAddress());
		setTimeouts(ctx);
		return port;
	}

	protected void preSetup() {
		if (isSkipSSLVerification())
			disableSSLVerification();
	}

	private void disableSSLVerification() {
		logger.warn("SSL certification verification is being disabled. This will allow this caTissue instance to connect to C3PR with invalid SSL certificates. Please use with caution.");
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(
					java.security.cert.X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(
					java.security.cert.X509Certificate[] certs, String authType) {
			}
		} };

		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}

		com.sun.net.ssl.HostnameVerifier hv = new com.sun.net.ssl.HostnameVerifier() {

			public boolean verify(String urlHostname, String certHostname) {
				return true;
			}
		};
		com.sun.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(hv);

		HostnameVerifier hv2 = new HostnameVerifier() {

			public boolean verify(String urlHostName, SSLSession session) {
				return true;
			}
		};
		HttpsURLConnection.setDefaultHostnameVerifier(hv2);

	}

	protected javax.xml.ws.spi.Provider getProvider()
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {

		ClassLoader classLoader;
		try {
			classLoader = Thread.currentThread().getContextClassLoader();
		} catch (Exception x) {
			throw new RuntimeException(x.toString(), x);
		}
		try {
			return (Provider) classLoader.loadClass(
					"com.sun.xml.internal.ws.spi.ProviderImpl").newInstance();
		} catch (ClassNotFoundException e) {
			return (Provider) classLoader.loadClass(
					"com.sun.xml.ws.spi.ProviderImpl").newInstance();
		}
	}

	/**
	 * @return the skipSSLVerification
	 */
	public boolean isSkipSSLVerification() {
		return skipSSLVerification;
	}

	/**
	 * @param skipSSLVerification
	 *            the skipSSLVerification to set
	 */
	public void setSkipSSLVerification(boolean skipSSLVerification) {
		this.skipSSLVerification = skipSSLVerification;
	}

}