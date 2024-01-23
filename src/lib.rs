use once_cell::sync::Lazy;
use reqwest::Client;

#[cfg(target_os = "android")]
mod android;

uniffi::setup_scaffolding!();

static HTTP: Lazy<Client> = Lazy::new(|| {
    #[cfg(target_os = "android")]
    {
        let jvm = crate::android::java_vm();
        let env = jvm.attach_current_thread_permanently().unwrap();
        crate::android::init_verifier(&env);
    }

    Client::builder()
        .use_rustls_tls()
        .use_preconfigured_tls(rustls_platform_verifier::tls_config())
        .build()
        .unwrap()
});

#[uniffi::export(async_runtime = "tokio")]
async fn get(url: String) -> String {
    HTTP.get(url).send().await.unwrap().text().await.unwrap()
}
