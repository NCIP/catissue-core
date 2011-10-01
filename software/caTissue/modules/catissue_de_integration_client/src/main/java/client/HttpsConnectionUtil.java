
package client;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;

public class HttpsConnectionUtil
{

	public static void trustAllHttpsCertificates() throws DynamicExtensionsSystemException
	{
		//  Create a trust manager that does not validate certificate chains:
		try
		{
			javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];

			trustAllCerts[0] = new DerivedTrustManager();

			javax.net.ssl.SSLContext sslContext = javax.net.ssl.SSLContext.getInstance("SSL");

			sslContext.init(null, trustAllCerts, null);

			javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sslContext
					.getSocketFactory());
			final HostnameVerifier hostVerifier = new HostnameVerifier()
			{

				public boolean verify(String hostname, SSLSession session)
				{
					// TODO Auto-generated method stub
					return true;
				}
			};
			HttpsURLConnection.setDefaultHostnameVerifier(hostVerifier);
		}
		catch (NoSuchAlgorithmException e)
		{
			throw new DynamicExtensionsSystemException(
					"Error occured while accepting the server certificates", e);
		}
		catch (KeyManagementException e)
		{
			throw new DynamicExtensionsSystemException(
					"Error occured while accepting the server certificates", e);
		}
	}

	/**
	 * This is the overridden version of trust manager which will trust all the https connections
	 * @author pavan_kalantri
	 *
	 */
	public static class DerivedTrustManager
			implements
				javax.net.ssl.TrustManager,
				javax.net.ssl.X509TrustManager
	{

		/* (non-Javadoc)
		 * @see javax.net.ssl.X509TrustManager#getAcceptedIssuers()
		 */
		public java.security.cert.X509Certificate[] getAcceptedIssuers()
		{
			return new java.security.cert.X509Certificate[0];
		}

		/**
		 * This method will always return true so that any url is considered to be trusted.
		 * & no certification validation is done.
		 * @param certs certificates.
		 * @return true if certificate is trusted
		 */
		public boolean isServerTrusted(java.security.cert.X509Certificate[] certs)
		{
			return true;
		}

		/**
		 * This method will always return true so that any url is considered to be trusted.
		 * & no certification validation is done.
		 * @param certs certificates.
		 * @return true if certificate is trusted
		 */
		public boolean isClientTrusted(java.security.cert.X509Certificate[] certs)
		{
			return true;
		}

		/* (non-Javadoc)
		 * @see javax.net.ssl.X509TrustManager#checkServerTrusted(java.security.cert.X509Certificate[], java.lang.String)
		 */
		public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException
		{
			// TODO Auto-generated catch block
		}

		/* (non-Javadoc)
		 * @see javax.net.ssl.X509TrustManager#checkClientTrusted(java.security.cert.X509Certificate[], java.lang.String)
		 */
		public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException
		{
			// TODO Auto-generated catch block
		}
	}
}
