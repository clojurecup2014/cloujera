# -*- mode: ruby -*-
# vi: set ft=ruby :

# Vagrantfile API/syntax version. Don't touch unless you know what you're doing!
VAGRANTFILE_API_VERSION = "2"

Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|
  config.vm.box = "ubuntu/trusty64"

  # ElasticSearch ports:
  config.vm.network "forwarded_port", guest: 9200, host: 9200 # http
  config.vm.network "forwarded_port", guest: 9300, host: 9300

  # Redis ports:
  config.vm.network "forwarded_port", guest: 6379, host: 6379

  # Cloujera port
  config.vm.network "forwarded_port", guest: 80, host: 8081

  # provisioning: docker, elasticsearch, redis
  config.vm.provision "shell", path: "./scripts/provision.sh"

  config.vm.provider "virtualbox" do |v|
      v.memory = 1024
  end
end
