use once_cell::sync::Lazy;
use reqwest::Client;

uniffi::setup_scaffolding!();

static HTTP: Lazy<Client> = Lazy::new(Client::new);

#[uniffi::export(async_runtime = "tokio")]
async fn get(url: String) -> String {
    HTTP.get(url).send().await.unwrap().text().await.unwrap()
}
